package com.library.networklib.log;

/**
 * @author Johnny
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {

    private final StringBuffer sb = new StringBuffer();

    @Override
    public void log(String message) {
        //TODO 格式化Http请求内容
    }
}
