package com.library.networklib.observer;

import android.app.Dialog;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import io.reactivex.disposables.Disposable;

/**
 * @author Johnny
 */
public abstract class ProgressObserver<T> extends DefaultObserver<T> {

    private static final String FRAGMENT_TAG = "LOADING_TAG";
    private DialogFragment mProgressDialog;
    private FragmentActivity mAttachActivity;

    public ProgressObserver(FragmentActivity attachActivity) {
        this.mAttachActivity = attachActivity;
        this.mProgressDialog = new DefaultProgressDialog.Builder().build();
    }

    public ProgressObserver(FragmentActivity attachActivity, DialogFragment dialog) {
        this.mAttachActivity = attachActivity;
        this.mProgressDialog = dialog;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.show(mAttachActivity.getSupportFragmentManager(), FRAGMENT_TAG);
        }
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissLoadingDialog();
    }

    private void dismissLoadingDialog() {
        if (this.mProgressDialog != null) {
            final Dialog dialog = this.mProgressDialog.getDialog();
            if (dialog != null && dialog.isShowing()) {
                this.mProgressDialog.dismissAllowingStateLoss();
            }
        }
    }
}
