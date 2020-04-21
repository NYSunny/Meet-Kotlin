package com.library.networklib.interceptors;

import java.io.IOException;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author niyang
 * @date 2019/3/25
 */
public abstract class BaseHeadersInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Request request = chain.request();
        final Map<String, String> headersMap = buildHeaders();
        if (headersMap == null || headersMap.size() <= 0) {
            return chain.proceed(request);
        }

        return chain.proceed(request.newBuilder()
                .headers(buildHeaders(request, headersMap))
                .build());
    }

    private Headers buildHeaders(Request request, Map<String, String> headersMap) {
        // 取出旧的headers
        final Headers headers = request.headers();
        // 重新构建Headers的builder，把原来headers中已有的参数加到新的builder
        final Headers.Builder builder = headers.newBuilder();
        // 这里增加新的header键值对
        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        // 构建新的headers
        return builder.build();
    }

    public abstract Map<String, String> buildHeaders();
}
