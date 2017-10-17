package com.mute.conversion.image.util;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.util.Map;

public class OkHttpUtil {
    private OkHttpUtil() {
    }

    private static OkHttpClient okHttpClient = createOkHttpClient();

    private static OkHttpUtil okHttpUtil = new OkHttpUtil();

    public static OkHttpUtil getInstance() {
        return okHttpUtil;
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient();
    }

    /**
     * 忽略证书并实例化okHttpClient
     *
     * @return okHttpClient
     */
    private static OkHttpClient createUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory,null);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应体
     */
    public Response doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    public Response doGet(String url, Map<String, String> params, Map<String, String> headers) {

        Response response = null;

        Request.Builder requestBuilder = new Request.Builder();

        if (url != null) {
            if (params != null && !params.isEmpty()) {
                url = getIntactUrl(url, params);
                requestBuilder.url(url);
            } else {
                requestBuilder.url(url);
            }
        } else {
            return null;
        }


        if (headers != null && headers.size() > 0) {
            Headers tempHeaders = Headers.of(headers);
            requestBuilder.headers(tempHeaders);
        }

        Request request = requestBuilder.get().build();
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @return 响应体
     */
    public Response doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * post请求
     *
     * @param url         请求地址
     * @param contentType Content-type 对照表http://tool.oschina.net/commons
     * @param content     请求正文
     * @return 响应体
     */
    public Response doPost(String url, Map<String, String> params, String contentType, String content, Map<String, String> headers) {

        return doPost(getIntactUrl(url, params), contentType, content, headers);
    }

    /**
     * post请求
     *
     * @param url         请求地址
     * @param contentType Content-type 对照表http://tool.oschina.net/commons
     * @param content     请求正文
     * @param headers     请求头
     * @return 响应体
     */
    public Response doPost(String url, String contentType, String content, Map<String, String> headers) {
        Response response = null;

        Request.Builder requestBuilder = new Request.Builder();

        if (url != null) {
            requestBuilder.url(url);
        }

        if (contentType != null && content != null) {
            MediaType mediaType = MediaType.parse(contentType);
            requestBuilder.post(RequestBody.create(mediaType, content));
        }

        if (headers != null && headers.size() > 0) {
            Headers tempHeaders = Headers.of(headers);
            requestBuilder.headers(tempHeaders);
        }

        Request request = requestBuilder.build();
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 通过请求地址和请求参数获取完整url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 完整的url
     */
    private String getIntactUrl(String url, Map<String, String> params) {
        String intactUrl = url;
        if (!(params == null || params.isEmpty())) {
            String paramsString = "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsString = paramsString.concat(entry.getKey()).concat("=").concat(entry.getValue()).concat("&");
            }
            paramsString = paramsString.substring(0, paramsString.length() - 1);
            intactUrl = intactUrl.concat(paramsString);
        }
        return intactUrl;
    }
}
