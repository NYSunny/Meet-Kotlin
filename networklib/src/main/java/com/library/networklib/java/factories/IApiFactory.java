package com.library.networklib.java.factories;

/**
 * @author niyang
 * @date 2019/3/26
 */
public interface IApiFactory {

    <T> T createApi(Class<T> apiClazz);

    <T> T createApiToMain(Class<T> apiClazz);
}
