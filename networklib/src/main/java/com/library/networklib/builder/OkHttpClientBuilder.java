package com.library.networklib.builder;

import com.library.networklib.log.HttpLogger;
import com.library.networklib.log.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author Johnny
 */
public class OkHttpClientBuilder {

    /**
     * seconds
     */
    private static final int DEFAULT_TIME_OUT = 10;

    private OkHttpClient.Builder okHttpClientBuilder;
    private boolean isDebug;
    private boolean isCache;
    private int readTimeOut;
    private int writeTimeOut;
    private int connectTimeOut;
    private Interceptor[] interceptors;

    public OkHttpClientBuilder() {
        this.okHttpClientBuilder = new OkHttpClient.Builder();
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

    public OkHttpClient build() {
        // 添加拦截器
        addInterceptors();
        // 设置OkHttp超时信息
        setTimeOut();
        // 设置Http日志
        setNetLogging();
        return this.okHttpClientBuilder.build();
    }

    private void addInterceptors() {
        if (this.interceptors != null) {
            for (Interceptor interceptor : this.interceptors) {
                this.okHttpClientBuilder.addInterceptor(interceptor);
            }
        }
    }

    private void setTimeOut() {
        this.okHttpClientBuilder.readTimeout(this.readTimeOut == 0 ? DEFAULT_TIME_OUT : this.readTimeOut, TimeUnit.SECONDS);
        this.okHttpClientBuilder.writeTimeout(this.writeTimeOut == 0 ? DEFAULT_TIME_OUT : this.writeTimeOut, TimeUnit.SECONDS);
        this.okHttpClientBuilder.connectTimeout(this.connectTimeOut == 0 ? DEFAULT_TIME_OUT : this.connectTimeOut, TimeUnit.SECONDS);
        this.okHttpClientBuilder.retryOnConnectionFailure(true);
    }

    private void setNetLogging() {
        if (this.isDebug) {
            final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            this.okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }
    }

    private void setCache() {
        if (this.isCache) {
            // TODO 处理缓存
        }
    }

}
