package com.zhr.cat.tools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {
    static {
        //proxy();
    }

    /**
     * 代理设置
     */
    private static void proxy() {
        System.setProperty("http.proxyHost", "172.18.199.13");
        System.setProperty("http.proxyPort", "9999");
        System.setProperty("http.nonProxyHosts", "10.* | 170.* | 172.* | 150.*");
    }

    /**
     * 信任所有SSL证书
     *
     * @throws HttpFailException
     */
    private static void initSSL() throws HttpFailException {
        try {
            SslUtils.ignoreSsl();
        } catch (Exception e) {
            throw new HttpFailException("信任所有SSL证书失败");
        }
    }

    public static String get(String url, String encode, int maxTimeout) throws HttpFailException {
        long start = System.currentTimeMillis();
        initSSL();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            if (maxTimeout > 0) {
                connection.setConnectTimeout(maxTimeout);
            }
            return getResultString(encode, start, connection);
        } catch (Exception err) {
            int stateCode = -100;
            if (connection != null) {
                try {
                    stateCode = connection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            throw new HttpFailException(stateCode, System.currentTimeMillis() - start, err);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String getResultString(String encode, long start, HttpURLConnection connection) throws IOException, HttpFailException {
        if (connection.getResponseCode() == 200) {
            BufferedReader br = null;
            if (encode == null) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));
            }
            StringBuffer result = new StringBuffer();
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                result.append(tmp);
                result.append("\n");
            }

            return result.toString();
        } else {
            throw new HttpFailException(connection.getResponseCode(), System.currentTimeMillis() - start);
        }
    }

    public static String get(String url) throws HttpFailException {
        return get(url, "utf-8", -1);
    }

    public static String get(String url, int maxTimeout) throws HttpFailException {
        return get(url, "utf-8", maxTimeout);
    }

    private static String post(String url, String data, String encode, int maxTimeout, boolean isJson)
            throws HttpFailException {
        long start = System.currentTimeMillis();
        initSSL();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setRequestProperty("accept", "application/json");
            if (isJson) {
                connection.setRequestProperty("Content-Type", "application/json");
            } else {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            connection.setRequestProperty("encoding", encode);
            if (maxTimeout > 0) {
                connection.setConnectTimeout(maxTimeout);
            }
            byte[] dataBytes = data.getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(dataBytes.length));
            OutputStream out = connection.getOutputStream();
            out.write(dataBytes);
            out.flush();
            out.close();
            return getResultString(encode, start, connection);
        } catch (Exception err) {
            int stateCode = -100;
            if (connection != null) {
                try {
                    stateCode = connection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            throw new HttpFailException(stateCode, System.currentTimeMillis() - start, err);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String post(String url, Map<String, String> params) throws HttpFailException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return post(url, sb.toString(), "utf-8", -1, false);
    }

    public static String post(String url, Map<String, String> params, int maxTimeout) throws HttpFailException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return post(url, sb.toString(), "utf-8", maxTimeout, false);
    }

    public static String post(String url, Map<String, String> params, String encode, int maxTimeout)
            throws HttpFailException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return post(url, sb.toString(), encode, maxTimeout, false);
    }
}
