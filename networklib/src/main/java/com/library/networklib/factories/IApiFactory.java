package com.library.networklib.factories;

/**
 * @author Johnny
 */
public interface IApiFactory {

    <T> T createApi(Class<T> apiClazz);

    <T> T createApiToMain(Class<T> apiClazz);
}
