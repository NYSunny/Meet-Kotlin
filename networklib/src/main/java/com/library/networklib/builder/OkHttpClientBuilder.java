package com.library.networklib.builder;

import com.library.networklib.auth.SSLHelper;
import com.library.networklib.log.HttpLogger;
import com.library.networklib.log.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * 带有默认值的OkHttpClientBuilder
 *
 * @author Johnny
 */
public class OkHttpClientBuilder {

    /**
     * seconds
     */
    private static final int DEFAULT_TIME_OUT = 10;

    private final OkHttpClient.Builder builder;
    private boolean isDebug;
    private boolean isCache;
    private int readTimeOut;
    private int writeTimeOut;
    private int connectTimeOut;
    private Interceptor[] interceptors;
    private SSLHelper.SSLParams sslParams;

    public OkHttpClientBuilder() {
        this.builder = new OkHttpClient.Builder();
    }

    public OkHttpClientBuilder setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public OkHttpClientBuilder setCache(boolean isCache) {
        this.isCache = isCache;
        return this;
    }

    public OkHttpClientBuilder setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public OkHttpClientBuilder setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public OkHttpClientBuilder setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public OkHttpClientBuilder addInterceptors(Interceptor... interceptors) {
        this.interceptors = interceptors;
        return this;
    }

    public OkHttpClientBuilder setSslParams(SSLHelper.SSLParams sslParams) {
        this.sslParams = sslParams;
        return this;
    }

    public OkHttpClient build() {
        // 添加拦截器
        addInterceptors();
        // 设置OkHttp超时信息
        setTimeOut();
        // 设置Http日志
        setNetLogging();
        setSslParams();
        return this.builder.build();
    }

    private void addInterceptors() {
        if (this.interceptors != null) {
            for (Interceptor interceptor : this.interceptors) {
                this.builder.addInterceptor(interceptor);
            }
        }
    }

    private void setTimeOut() {
        this.builder.readTimeout(this.readTimeOut == 0 ? DEFAULT_TIME_OUT : this.readTimeOut, TimeUnit.SECONDS);
        this.builder.writeTimeout(this.writeTimeOut == 0 ? DEFAULT_TIME_OUT : this.writeTimeOut, TimeUnit.SECONDS);
        this.builder.connectTimeout(this.connectTimeOut == 0 ? DEFAULT_TIME_OUT : this.connectTimeOut, TimeUnit.SECONDS);
        this.builder.retryOnConnectionFailure(true);
    }

    private void setNetLogging() {
        if (this.isDebug) {
            final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            this.builder.addInterceptor(httpLoggingInterceptor);
        }
    }

    private void setSslParams() {
        if (this.sslParams != null) {
            this.builder.sslSocketFactory(this.sslParams.sslSocketFactory, this.sslParams.x509TrustManager);
        } else {
            final SSLHelper.SSLParams sslParams = SSLHelper.getSSLParams();
            this.builder.sslSocketFactory(sslParams.sslSocketFactory, sslParams.x509TrustManager);
        }
    }

    private void setCache() {
        if (this.isCache) {
            // TODO 处理缓存
        }
    }

}
