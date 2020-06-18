package com.library.networklib.kit;

/**
 * @author Johnny
 */
public interface IApiFactory {

    <T> T createApi(Class<T> apiClazz);

    <T> T createApiToMain(Class<T> apiClazz);
}
