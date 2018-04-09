package com.zhr.cat.tools.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSASecurity {
	private static ThreadLocal<Map<String, Cipher>> rsaEncryptCipherThreadLocal = new ThreadLocal<Map<String, Cipher>>();
	private static ThreadLocal<Map<String, Cipher>> rsaDecryptCipherThreadLocal = new ThreadLocal<Map<String, Cipher>>();

	private static Map<String, Cipher> getEncryptCipherMap() {
		Map<String, Cipher> df = rsaEncryptCipherThreadLocal.get();
		if (df == null) {
			df = new HashMap<String, Cipher>();
			rsaEncryptCipherThreadLocal.set(df);
		}
		return df;
	}

	private static Map<String, Cipher> getDecryptCipherMap() {
		Map<String, Cipher> df = rsaDecryptCipherThreadLocal.get();
		if (df == null) {
			df = new HashMap<String, Cipher>();
			rsaDecryptCipherThreadLocal.set(df);
		}
		return df;
	}

	private static Cipher getRsaPubKeyCipher(String key) throws Exception {
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		key = key.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace(" ", "")
				.replace("\r", "").replace("\n", "");
		Map<String, Cipher> map = getEncryptCipherMap();
		if (map.containsKey(key)) {
			return map.get(key);
		}
		byte[] data = Base64.getDecoder().decode(key);
		PublicKey k = keyf.generatePublic(new X509EncodedKeySpec(data));
		Cipher cipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		cipher.init(Cipher.ENCRYPT_MODE, k);
		map.put(key, cipher);
		return cipher;
	}

	private static Cipher getRsaPrivateKeyCipher(String key) throws Exception {
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		key = key.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replace(" ", "")
				.replace("\r", "").replace("\n", "");
		Map<String, Cipher> map = getDecryptCipherMap();
		if (map.containsKey(key)) {
			return map.get(key);
		}
		byte[] data = Base64.getDecoder().decode(key);
		PrivateKey k = keyf.generatePrivate(new PKCS8EncodedKeySpec(data));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, k);
		map.put(key, cipher);
		return cipher;
	}

	/**
	 * 使用指定的公钥对数据进行加密 RSA填充方式为 RSA/ECB/PKCS1Padding 本方法线程安全
	 * 
	 * @param data
	 *            待加密数据
	 * @param pubKey
	 *            公钥字符串(可包含BEGIN PUBLIC KEY 也可不包含)
	 * @return
	 */
	public static byte[] rsaEncrypt(byte[] data, String pubKey) {
		try {
			return getRsaPubKeyCipher(pubKey).doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("加密失败", e);
		}
	}

	/**
	 * 使用指定的私钥对数据进行解密 RSA填充方式为 RSA/ECB/PKCS1Padding 本方法线程安全
	 * 
	 * @param data
	 *            待解密数据
	 * @param priKey
	 *            私钥字符串 必须为PKCS#8格式 (可包含BEGIN PRIVATE KEY 也可不包含)
	 * @return
	 */
	public static byte[] rsaDecrypt(byte[] data, String priKey) {
		try {
			return getRsaPrivateKeyCipher(priKey).doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("解密失败", e);
		}
	}
}
