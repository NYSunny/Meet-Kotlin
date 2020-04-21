package com.library.networklib.java.observer;

import com.library.networklib.java.exceptions.ExceptionHandler;
import com.library.networklib.java.exceptions.NetError;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author niyang
 * @date 2019/3/26
 */
public abstract class DefaultObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFail(ExceptionHandler.handleException(e));
    }

    @Override
    public void onComplete() {
        // Nothing to do
    }

    public abstract void onSuccess(T t);

    public void onFail(NetError error) {
        // Nothing to do
    }
}
