package com.zhr.cat.tools.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Security {

	public static String toMD5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return toMD5(str, "utf-8");
	}

	public static String toMD5(String str, String charset)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return new String(MessageDigest.getInstance("MD5").digest(str.toString().getBytes(charset)));
	}

	private static String toHex(byte[] bytes) {
		String str2 = "0123456789ABCDEF";
		final char[] HEX_DIGITS = str2.toCharArray();
		StringBuilder ret = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
			ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		return ret.toString();
	}

	public static String toMD5Hex(String str, String charset)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return toHex(MessageDigest.getInstance("MD5").digest(str.toString().getBytes(charset)));
	}

	public static String toMD5Hex(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return toMD5Hex(str, "utf-8");
	}
}
