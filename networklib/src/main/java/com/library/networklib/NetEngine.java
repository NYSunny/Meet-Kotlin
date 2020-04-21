package com.library.networklib;

import com.library.networklib.factories.IApiFactory;
import com.library.networklib.transformer.MainThreadTransformer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Https通讯原理
 * https://blog.csdn.net/u010739551/article/details/79287236
 * HTTPS整套加密机制是如何实现的？
 * https://www.wosign.com/news/httpsjiami_20180817.htm
 * Https原理和实现
 * https://blog.csdn.net/weixin_35973945/article/details/82021116
 * https://www.cnblogs.com/blogs-of-lxl/p/10136582.html
 *
 * @author niyang
 * @date 2019/3/25
 */
public final class NetEngine implements IApiFactory {

    private static final String NORMAL_TAG = "NORMAL_TAG";
    private static final String MAIN_THREAD_TAG = "MAIN_THREAD_TAG";
    private Retrofit retrofit;
    private Map<String, Object> apiCache;

    public void setupRetrofit(Retrofit retrofit) {
        if (retrofit == null) {
            throw new NullPointerException("param retrofit is not null!");
        }
        this.retrofit = retrofit;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createApi(Class<T> apiClazz) {
        if (this.apiCache == null) {
            this.apiCache = new HashMap<>();
        }
        final String key = NORMAL_TAG + '_' + apiClazz.getName();
        T result = (T) this.apiCache.get(key);
        if (result == null) {
            result = this.retrofit.create(apiClazz);
            this.apiCache.put(key, result);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createApiToMain(final Class<T> apiClazz) {
        if (this.apiCache == null) {
            this.apiCache = new HashMap<>();
        }
        final String key = MAIN_THREAD_TAG + '_' + apiClazz.getName();
        T result = (T) this.apiCache.get(key);
        if (result == null) {
            result = (T) Proxy.newProxyInstance(apiClazz.getClassLoader(), new Class[]{apiClazz}, new InvocationHandler() {
                T t = retrofit.create(apiClazz);

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return ((Observable) Proxy.getInvocationHandler(t).invoke(proxy, method, args)).compose(new MainThreadTransformer());
                }
            });
            this.apiCache.put(key, result);
        }
        return result;
    }
}
