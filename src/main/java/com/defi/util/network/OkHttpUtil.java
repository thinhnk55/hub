package com.defi.util.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    public static JsonObject callRequest(Request request) {
        try {
            String s = callRequestStringResponse(request);
            if(s != null) {
                JsonObject json = GsonUtil.toJsonObject(s);
                return json;
            }
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
            DebugLogger.logger.error("callRequestStringResponse: {}, {}, {}" + request.url(), request.method(), request.body());
        }
        return null;
    }

    public static JsonArray callRequestArrayResponse(Request request) {
        try {
            String s = callRequestStringResponse(request);
            if(s != null) {
                JsonArray array = GsonUtil.toJsonArray(s);
                return array;
            }
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
            DebugLogger.logger.error("callRequestStringResponse: {}, {}, {}" + request.url(), request.method(), request.body());
        }
        return null;
    }

    public static String callRequestStringResponse(Request request) {
        try {
            Response response = callRequestFullResponse(request);
            if(response != null) {
                String res = response.body().string();
                response.body().close();
                return res;
            }
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static Response callRequestFullResponse(Request request) {
        try {
            OkHttpClient client;
            if (request.url().isHttps()) {
                client = httpsClient();
            } else {
                client = httpClient();
            }
            Response response = client.newCall(request).execute();
            return response;
        } catch (Exception e) {
            return null;
        }
    }
    public static String getStringResponse(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            return callRequestStringResponse(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static JsonObject get(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            return callRequest(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }
    public static JsonArray getAsArray(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            return callRequestArrayResponse(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static JsonObject get(String url, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .get()
                    .build();
            return callRequest(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static Response getFullResponse(String url, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .get()
                    .build();
            return callRequestFullResponse(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static Response postFullResponse(String url, String jsonBody, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        try {
            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .post(body)
                    .build();
            return callRequestFullResponse(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }
    public static JsonObject postJson(String url, String jsonBody) {
        try {
            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            return callRequest(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static JsonObject postJson(String url, String jsonBody, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        try {
            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .post(body)
                    .build();
            return callRequest(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static JsonObject postForm(String url, RequestBody form, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .post(form)
                    .build();
            return callRequest(request);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        return null;
    }

    public static OkHttpClient httpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        return builder.build();
    }

    public static OkHttpClient httpsClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
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
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.logger.error(stacktrace);
        }
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        return builder.build();
    }
}
