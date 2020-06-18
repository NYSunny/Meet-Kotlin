package com.library.networklib.kit;

import com.library.networklib.builder.OkHttpClientBuilder;
import com.library.networklib.builder.RetrofitBuilder;
import com.library.networklib.interceptors.DefaultHeadersInterceptor;
import com.library.networklib.sp.NetSP;

import java.util.Map;

import io.reactivex.annotations.Nullable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Johnny
 */
public final class NetKit implements IApiFactory {

    private static final int TIME_OUT = 20;
    @Nullable
    private NetEngine mNetEngine;

    private NetKit() {
    }

    public NetKit setHeaders(Map<String, String> headers) {
        NetSP.getInstance().saveNetHeaderParams(headers);
        return this;
    }

    public void init(String baseUrl) {
        this.mNetEngine = new NetEngine();
        final OkHttpClient okHttpClient = new OkHttpClientBuilder()
                .addInterceptors(new DefaultHeadersInterceptor())
                .setDebug(true)
                .setConnectTimeOut(TIME_OUT)
                .setReadTimeOut(TIME_OUT)
                .setWriteTimeOut(TIME_OUT)
                .build();
        final Retrofit retrofit = new RetrofitBuilder()
                .setBaseUrl(baseUrl)
                .setOkHttpClient(okHttpClient)
                .build();
        this.mNetEngine.setupRetrofit(retrofit);
    }

    public void init(Retrofit retrofit) {
        this.mNetEngine = new NetEngine();
        this.mNetEngine.setupRetrofit(retrofit);
    }

    private static class Holder {
        private static final NetKit INSTANCE = new NetKit();
    }

    public static NetKit getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public <T> T createApi(Class<T> apiClazz) {
        if (this.mNetEngine != null) {
            return this.mNetEngine.createApi(apiClazz);
        } else {
            throw new IllegalStateException("NetKit is not initialized！");
        }
    }

    @Override
    public <T> T createApiToMain(Class<T> apiClazz) {
        if (this.mNetEngine != null) {
            return this.mNetEngine.createApiToMain(apiClazz);
        } else {
            throw new IllegalStateException("NetKit is not initialized！");
        }
    }
}
