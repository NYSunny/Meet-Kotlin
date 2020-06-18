package com.library.networklib.exceptions;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.HttpException;

/**
 * @author Johnny
 */
public class ExceptionHandler {

    public static NetError handleException(Throwable throwable) {
        final NetError netError = new NetError();
        if (throwable instanceof HttpException) {
            final HttpException exception = (HttpException) throwable;
            netError.errorCode = exception.code();
            netError.httpErrorMsg = exception.message();

            final ResponseBody responseBody = exception.response().errorBody();
            if (responseBody != null) {
                try {
                    final BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE);
                    Buffer buffer = source.buffer();

                    Charset charset = StandardCharsets.UTF_8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(StandardCharsets.UTF_8);
                    }
                    if (isPlaintext(buffer)) {
                        netError.serverErrorMsg = buffer.clone().readString(charset);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return netError;
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
