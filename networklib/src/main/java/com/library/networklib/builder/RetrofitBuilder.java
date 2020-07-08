package com.library.networklib.builder;

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
        return new OkHttpClientBuilder().build();
    }
}
