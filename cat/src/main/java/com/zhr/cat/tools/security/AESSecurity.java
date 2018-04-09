package com.zhr.cat.tools.security;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密类 该类线程安全
 * 
 *
 */
public class AESSecurity {
	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法

	private ThreadLocal<Cipher> encryptCipher = new ThreadLocal<Cipher>();
	private ThreadLocal<Cipher> decryptCipher = new ThreadLocal<Cipher>();

	private String key;

	public AESSecurity(String key) {
		this.key = key;
	}

	private Cipher getEncryptCipher() {
		try {
			Cipher cipher = encryptCipher.get();
			if (cipher == null) {
				cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
				// 创建密码器
				cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
				// 初始化为加密模式的密码器

				encryptCipher.set(cipher);
			}
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException("创建加密器失败", e);
		}
	}

	private Cipher getDecryptCipher() {
		try {
			Cipher cipher = decryptCipher.get();
			if (cipher == null) {
				cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
				// 创建密码器
				cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
				// 初始化为加密模式的密码器
				decryptCipher.set(cipher);
			}
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException("创建解密器失败", e);
		}
	}

	/**
	 * AES 加密操作
	 *
	 * @param content
	 *            待加密内容 加密方法会先将其进行getByte操作
	 * @return 返回Base64转码后的加密数据
	 */
	public String encrypt(String content) {
		try {
			Cipher cipher = getEncryptCipher();
			byte[] byteContent = content.getBytes("utf-8");
			byte[] result = cipher.doFinal(byteContent);// 加密
			return Base64.getEncoder().encodeToString(result);// 通过Base64转码返回
		} catch (Exception ex) {

			System.err.println("加密失败" + ex);
		}
		return null;
	}

	/**
	 * AES 解密操作
	 *
	 * @param content
	 *            待解密内容(Base64格式)
	 * @return 解密后的数据的new String()后的结果
	 */
	public String decrypt(String content) {

		try {
			Cipher cipher = getDecryptCipher();
			// 执行操作
			byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
			return new String(result, "utf-8");
		} catch (Exception ex) {
			System.err.println("解密失败" + ex);
		}
		return null;
	}

	/**
	 * 生成加密秘钥
	 *
	 * @return
	 */
	private SecretKeySpec getSecretKey(final String password) throws Exception {
		if (password.length() != 16) {
			throw new RuntimeException("AES密钥长度必须为16");
		}
		// 返回生成指定算法密钥生成器的 KeyGenerator 对象
		// KeyGenerator kg = null;

		// try {
		// kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		//
		// //AES 要求密钥长度为 128
		// kg.init(128, new SecureRandom(password.getBytes()));
		//
		// //生成一个密钥
		// SecretKey secretKey = kg.generateKey();

		// return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
		return new SecretKeySpec(password.getBytes("utf-8"), KEY_ALGORITHM);
		// } catch (NoSuchAlgorithmException ex) {
		// logger.error("加密密钥生成失败",ex);
		// }
		//
		// return null;
	}

	
}
