package com.library.networklib.java.log;

/**
 * @author niyang
 * @date 2019/3/25
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {

    private final StringBuffer sb = new StringBuffer();

    @Override
    public void log(String message) {
        //TODO 格式化Http请求内容
    }
}
