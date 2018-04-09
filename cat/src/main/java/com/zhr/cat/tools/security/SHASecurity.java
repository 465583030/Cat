package com.zhr.cat.tools.security;

import java.security.MessageDigest;

/**
 * 通用加密与签名类
 * 
 * 
 */
public class SHASecurity {

	private static String sha(String decript, String type, String encoding) {
		try {
			MessageDigest digest = MessageDigest.getInstance(type);
			digest.update(decript.getBytes(encoding));
			byte[] messageDigest = digest.digest();

			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static String toSHA1(String str) {
		return sha(str, "SHA-1", "utf-8");
	}

	public static String toSHA1(String str, String charset) {
		return sha(str, "SHA-1", charset);
	}

	public static String toSHA256(String str) {
		return sha(str, "SHA-256", "utf-8");
	}

	public static String toSHA256(String str, String charset) {
		return sha(str, "SHA-256", charset);
	}

	public static String toSHA512(String str) {
		return sha(str, "SHA-512", "utf-8");
	}

	public static String toSHA512(String str, String charset) {
		return sha(str, "SHA-512", charset);
	}

//	private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

//	public static String toBase64(byte[] data) {
//		char[] out = new char[((data.length + 2) / 3) * 4];
//		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
//			boolean quad = false;
//			boolean trip = false;
//			int val = (0xFF & (int) data[i]);
//			val <<= 8;
//			if ((i + 1) < data.length) {
//				val |= (0xFF & (int) data[i + 1]);
//				trip = true;
//			}
//			val <<= 8;
//			if ((i + 2) < data.length) {
//				val |= (0xFF & (int) data[i + 2]);
//				quad = true;
//			}
//			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
//			val >>= 6;
//			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
//			val >>= 6;
//			out[index + 1] = alphabet[val & 0x3F];
//			val >>= 6;
//			out[index + 0] = alphabet[val & 0x3F];
//		}
//
//		return new String(out);
//	}
}