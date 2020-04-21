package com.library.networklib.builder;

import com.library.networklib.auth.SSLHelper;
import com.library.networklib.log.HttpLogger;
import com.library.networklib.log.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Johnny
 */
public class RetrofitBuilder {

    private static final int TIME_OUT = 10;
    private String baseUrl;
    private CallAdapter.Factory[] callAdapterFactories;
    private Converter.Factory[] converterFactories;
    private OkHttpClient okHttpClient;

    public RetrofitBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RetrofitBuilder setCallAdapterFactories(CallAdapter.Factory... factories) {
        this.callAdapterFactories = factories;
        return this;
    }

    public RetrofitBuilder setConverterFactories(Converter.Factory... factories) {
        this.converterFactories = factories;
        return this;
    }

    public RetrofitBuilder setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    public Retrofit build() {
        final Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(this.baseUrl);
        if (this.callAdapterFactories == null || this.callAdapterFactories.length <= 0) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        } else {
            for (CallAdapter.Factory factory : this.callAdapterFactories) {
                builder.addCallAdapterFactory(factory);
            }
        }
        if (this.converterFactories == null || this.converterFactories.length <= 0) {
            // TODO GsonConverterFactory.create(xxxx) 可自定义Gson转换
            builder.addConverterFactory(GsonConverterFactory.create());
        } else {
            for (Converter.Factory factory : this.converterFactories) {
                builder.addConverterFactory(factory);
            }
        }
        if (this.okHttpClient == null) {
            this.okHttpClient = createDefaultOkHttpClient();
        }
        builder.client(this.okHttpClient);
        return builder.build();
    }

    private OkHttpClient createDefaultOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 超时设置
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        // 默认不安全的ssl
        final SSLHelper.SSLParams sslParams = SSLHelper.getSSLParams();
        builder.sslSocketFactory(sslParams.sslSocketFactory, sslParams.x509TrustManager);
        // 网络日志
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);

        return builder.build();
    }

}
