package com.zhr.cat.tools.security;

import com.zhr.cat.tools.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SignSecurity {
	private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

	private static SimpleDateFormat getDateFormat() {
		SimpleDateFormat df = threadLocal.get();
		if (df == null) {
			df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
			threadLocal.set(df);
		}
		return df;
	}

	/**
	 * 使用给定的签名时间对字典数据进行签名验证 本方法自动过滤map中的无效数据与签名字段
	 * 
	 * @param map
	 *            请求参数
	 * @param token
	 * @return
	 */
	public static boolean validate(Map<String, String> map, String token) {
		String clientId = map.get("clientId");
		String timeStr = map.get("signTime");
		String sign = map.get("sign");
		if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(timeStr) || StringUtils.isEmpty(sign)) {
			return false;
		}
		Map<String, String> tmp = new HashMap<String, String>();
		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> kv = iterator.next();
			if (!"sign".equals(kv.getKey())) {
				tmp.put(kv.getKey().toLowerCase(), kv.getValue());
			}
		}
		try {
			Date time = getDateFormat().parse(timeStr);
			return sign.equals(sign(tmp, clientId, token, time));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 使用给定的签名时间对字典数据进行签名 本方法自动过滤map中的空数据
	 * 
	 * @param map
	 *            待签名数据
	 * @param clientId
	 *            客户端ID
	 * @param token
	 *            签名秘钥
	 * @param date
	 *            签名时间
	 * @return
	 */
	public static String sign(Map<String, String> map, String clientId, String token, Date date) {
		Map<String, String> tmp = new HashMap<String, String>();
		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> kv = iterator.next();
			if (StringUtils.isNotEmpty(kv.getValue())) {
				tmp.put(kv.getKey().toLowerCase(), kv.getValue().trim());
			}
		}
		tmp.put("clientid", clientId);
		tmp.put("signtime", getDateFormat().format(date));

		String[] keys = (String[]) tmp.keySet().toArray(new String[tmp.size()]);

		Arrays.sort(keys);

		StringBuffer sb = new StringBuffer();
		for (String k : keys) {
			String value = (String) tmp.get(k);
			sb.append("&");
			sb.append(k.toLowerCase());
			sb.append("=");
			sb.append(value);
		}
		sb.append("&");
		sb.append(token);
		return SHASecurity.toSHA1(sb.substring(1)).toUpperCase();
	}
}
