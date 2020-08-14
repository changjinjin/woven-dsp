package com.info.baymax.access.dataapi.utils;

import com.google.gson.Gson;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpUtils {

    /**
     * 默认字符集
     */
    private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 默认读超时时间
     */
    private static final int DEFAULT_READTIMEOUT = 30000;
    /**
     * 默认链接超时时间
     */
    private static final int DEFAULT_CONNECTTIMEOUT = 30000;

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public static String httpsPost(String urlStr, String params, String charSet) {
        HttpsURLConnection httpsConn = null;
        try {
            byte[] data = params.getBytes(charSet);
            URL url = new URL(urlStr);
            HttpsHandler.trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setRequestMethod("POST");
            httpsConn.setRequestProperty("ContentType", "application/x-www-form-urlencoded");
            httpsConn.setRequestProperty("Content-Length", String.valueOf(data.length));
            httpsConn.setDoInput(true);
            httpsConn.setDoOutput(true);
            httpsConn.setConnectTimeout(30000);// jdk 1.5换成这个,连接超时
            httpsConn.setReadTimeout(30000);// jdk 1.5换成这个,读操作超时
            httpsConn.connect();
            OutputStream out = httpsConn.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
            return getResponseResult(httpsConn, urlStr, charSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (null != httpsConn) {
                httpsConn.disconnect();
            }
        }
    }

    public static String httpsPostJson(String urlStr, String params, String charSet) {
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) ((new URL(urlStr).openConnection()));
            httpConn.setDoOutput(true);
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setRequestMethod("POST");
            httpConn.connect();
            byte[] outputBytes = params.getBytes(charSet);
            OutputStream out = httpConn.getOutputStream();
            out.write(outputBytes);
            out.close();
            return getResponseResult(httpConn, urlStr, charSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (null != httpConn) {
                httpConn.disconnect();
            }
        }
    }

    public static String httpsGet(String urlStr, String params, String charSet) {
        HttpsURLConnection httpsConn = null;
        try {
            if (null != params && params.length() > 0) {
                if (urlStr.indexOf("?") == -1) {
                    urlStr += "?" + params;
                } else {
                    urlStr += "&" + params;
                }
            }
            byte[] data = null;
            if (params != null) {
                data = params.getBytes(charSet);
            }
            URL url = new URL(urlStr);
            HttpsHandler.trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setRequestMethod("GET");
            httpsConn.setRequestProperty("ContentType", "application/x-www-form-urlencoded");
            httpsConn.setRequestProperty("Content-Length", String.valueOf(data == null ? 0 : data.length));
            httpsConn.setDoInput(true);
            httpsConn.setDoOutput(true);
            httpsConn.setConnectTimeout(30000);// jdk 1.5换成这个,连接超时
            httpsConn.setReadTimeout(30000);// jdk 1.5换成这个,读操作超时
            httpsConn.connect();
            return getResponseResult(httpsConn, urlStr, charSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (null != httpsConn) {
                httpsConn.disconnect();
            }
        }
    }

    public static String getResponseResult(HttpURLConnection httpConn, String urlStr, String charSet) {
        String res = "";
        InputStreamReader in = null;
        BufferedReader buffer = null;
        String inputLine = null;
        try {
            // 获得响应状态
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                in = new InputStreamReader(httpConn.getInputStream(), charSet);
                buffer = new BufferedReader(in);
                while (((inputLine = buffer.readLine()) != null)) {
                    res += inputLine + "\n";
                }

                // byte[] buffer = new byte[1024];
                // int len = -1;
                // InputStream is = httpConn.getInputStream();
                // ByteArrayOutputStream bos = new ByteArrayOutputStream();
                // while ((len = is.read(buffer)) != -1) {
                // bos.write(buffer, 0, len);
                // }
                // res = bos.toString(charSet);
                // is.close();
                // bos.close();
            } else {
                throw new RuntimeException("error response code:" + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /****************************** 新加方法 ******************************/
    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr         请求地址
     * @param queryString    参数列表
     * @param headers        请求头信息列表
     * @param method         请求方式：POST|GET
     * @param connectTimeout 连接超时
     * @param readTimeout    读写超时
     * @param charSet        编码
     */
    public static String httpRequest(String urlStr, String queryString, Map<String, String> headers, String method,
                                     int connectTimeout, int readTimeout, String charSet) {
        HttpURLConnection httpConn = null;
        OutputStreamWriter out = null;
        try {
            if ("GET".equalsIgnoreCase(method)) {
                urlStr = urlStr + "?" + queryString;
            }

            httpConn = (HttpURLConnection) ((new URL(urlStr).openConnection()));
            httpConn.setRequestMethod(method); // 设置请求方法：POST或者GET
            httpConn.setConnectTimeout(connectTimeout);// 连接超时 单位毫秒
            httpConn.setReadTimeout(readTimeout);// 读取超时 单位毫秒
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            // httpConn.setUseCaches(false);
            // httpConn.setInstanceFollowRedirects(true);
            // httpConn.setChunkedStreamingMode(5);

            // 设置头信息
            String key = null;
            if (headers != null) {
                Iterator<String> iterator = headers.keySet().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    httpConn.setRequestProperty(key, headers.get(key));
                }
            }
            // httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Charset", defaultIfEmpty(charSet, DEFAULT_CHARSET));
            httpConn.setRequestProperty("Accept-Charset", defaultIfEmpty(charSet, DEFAULT_CHARSET));
            httpConn.setRequestProperty("Content-Length",
                String.valueOf(defaultIfEmpty(queryString, "").getBytes().length));
            httpConn.connect();

            if (!"GET".equalsIgnoreCase(method)) {
                out = new OutputStreamWriter(httpConn.getOutputStream(), charSet);
                out.write(queryString);
                out.flush();
            }

            return getResponseResult(httpConn, urlStr, charSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (null != httpConn) {
                httpConn.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr         请求地址
     * @param params         参数列表
     * @param headers        请求头信息列表
     * @param method         请求方式：POST|GET
     * @param connectTimeout 连接超时
     * @param readTimeout    读写超时
     * @param charSet        编码
     * @param isJson         是否发送json请求
     * @throws UnsupportedEncodingException
     */
    public static String httpRequest(String urlStr, Map<String, Object> params, Map<String, String> headers,
                                     String method, int connectTimeout, int readTimeout, String charSet, boolean isJson) {
        // 组合参数
        String queryString = "";
        String key = "";
        try {
            if (params != null) {
                Iterator<String> ite = params.keySet().iterator();
                if (isJson) {
                    queryString = new Gson().toJson(params);
                } else {
                    while (ite.hasNext()) {
                        key = ite.next();
                        queryString += key + "=" + URLEncoder.encode(params.get(key).toString(), DEFAULT_CHARSET) + "&";
                    }
                    if (queryString.endsWith("&")) {
                        queryString = queryString.substring(0, queryString.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return httpRequest(urlStr, queryString, headers, method, connectTimeout, readTimeout, charSet);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr         请求地址
     * @param params         参数列表
     * @param headers        请求头信息列表
     * @param connectTimeout 链接超时
     * @param readTimeout    读写超时
     * @param charSet        编码
     */
    public static String httpPost(String urlStr, Map<String, Object> params, Map<String, String> headers,
                                  int connectTimeout, int readTimeout, String charSet) {
        if (headers == null)
            headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=" + charSet);
        return httpRequest(urlStr, params, headers, "POST", connectTimeout, readTimeout, charSet, false);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr  请求地址
     * @param params  参数列表
     * @param headers 请求头信息
     * @param charSet 编码
     */
    public static String httpPost(String urlStr, Map<String, Object> params, Map<String, String> headers,
                                  String charSet) {
        return httpPost(urlStr, params, headers, DEFAULT_CONNECTTIMEOUT, DEFAULT_READTIMEOUT, charSet);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr  请求地址
     * @param params  参数列表
     * @param headers 请求头信息
     * @param charSet 编码
     */
    public static String httpPost(String urlStr, Map<String, Object> params, Map<String, String> headers) {
        return httpPost(urlStr, params, headers, DEFAULT_CHARSET);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr         请求地址
     * @param jsonStr        参数json串
     * @param headers        请求头信息
     * @param connectTimeout 链接超时时间
     * @param readTimeout    读写超时时间
     * @param charSet        编码
     */
    public static String httpPostJson(String urlStr, String jsonStr, Map<String, String> headers, int connectTimeout,
                                      int readTimeout, String charSet) {
        if (headers == null)
            headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;charset=" + charSet);
        headers.put("Accept", "application/json");
        return httpRequest(urlStr, jsonStr, headers, "POST", connectTimeout, readTimeout, charSet);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr  请求地址
     * @param jsonStr 参数json串
     * @param headers 请求头信息
     */
    public static String httpPostJson(String urlStr, String jsonStr, Map<String, String> headers) {
        return httpPostJson(urlStr, jsonStr, headers, DEFAULT_CONNECTTIMEOUT, DEFAULT_READTIMEOUT, DEFAULT_CHARSET);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr         请求地址
     * @param params         参数列表
     * @param headers        请求头信息
     * @param connectTimeout 链接超时时间
     * @param readTimeout    读写超时时间
     * @param charSet        编码
     */
    public static String httpPostJson(String urlStr, Map<String, Object> params, Map<String, String> headers,
                                      int connectTimeout, int readTimeout, String charSet) {
        return httpPostJson(urlStr, new Gson().toJson(params), headers, connectTimeout, readTimeout, charSet);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr  请求地址
     * @param params  参数列表
     * @param headers 请求头信息
     * @param charSet 编码
     */
    public static String httpPostJson(String urlStr, Map<String, Object> params, Map<String, String> headers,
                                      String charSet) {
        return httpPostJson(urlStr, params, headers, DEFAULT_CONNECTTIMEOUT, DEFAULT_READTIMEOUT, charSet);
    }

    /**
     * 说明：发送post请求. <br>
     *
     * @param urlStr  请求地址
     * @param params  参数列表
     * @param headers 请求头信息
     * @param charSet 编码
     */
    public static String httpPostJson(String urlStr, Map<String, Object> params, Map<String, String> headers) {
        return httpPostJson(urlStr, params, headers, DEFAULT_CHARSET);
    }

    /**
     * 说明：发送get请求. <br>
     *
     * @param urlStr         请求地址
     * @param params         参数列表
     * @param headers        请求头信息
     * @param connectTimeout 链接超时时间
     * @param readTimeout    读写超时时间
     * @param charSet        编码
     */
    public static String httpGet(String urlStr, Map<String, Object> params, Map<String, String> headers,
                                 int connectTimeout, int readTimeout, String charSet) {
        return httpRequest(urlStr, params, headers, "GET", connectTimeout, readTimeout, charSet, false);
    }

    /**
     * 说明：发送get请求. <br>
     *
     * @param urlStr  请求地址
     * @param params  参数列表
     * @param headers 请求头信息
     * @param charSet 编码
     */
    public static String httpGet(String urlStr, Map<String, Object> params, Map<String, String> headers,
                                 String charSet) {
        return httpGet(urlStr, params, headers, DEFAULT_CONNECTTIMEOUT, DEFAULT_READTIMEOUT, charSet);
    }

    /**
     * 说明：发送get请求. <br>
     * <p>
     *
     * @param urlStr  请求地址
     * @param params  参数列表
     * @param headers 请求头信息
     * @param charSet 编码
     */
    public static String httpGet(String urlStr, Map<String, Object> params, Map<String, String> headers) {
        return httpGet(urlStr, params, headers, DEFAULT_CHARSET);
    }

    /**
     * 说明：发送get请求. <br>
     * <p>
     *
     * @param urlStr 请求地址
     * @param params 参数列表
     */
    public static String httpGet(String urlStr, Map<String, Object> params) {
        return httpGet(urlStr, params, new HashMap<String, String>(), DEFAULT_CHARSET);
    }

    /**
     * 说明：发送get请求. <br>
     * <p>
     *
     * @param urlStr 请求地址
     * @date 2017-5-12 上午11:54:01
     */
    public static String httpGet(String urlStr) {
        return httpGet(urlStr, null, new HashMap<String, String>(), DEFAULT_CHARSET);
    }

    private static String defaultIfEmpty(String src, String defaultValue) {
        if (src != null && !src.isEmpty()) {
            return src;
        }
        return defaultValue;
    }
}
