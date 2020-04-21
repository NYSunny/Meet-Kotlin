package com.library.networklib.observer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.library.networklib.R;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Johnny
 */
public class DefaultProgressDialog extends DialogFragment {

    private boolean cancelable;
    private boolean canceledOnTouchOutside;
    @ColorInt
    private int outsideBackground;
    private boolean showLoadingText;
    private String showLoadingTextContent;

    public DefaultProgressDialog() {
    }

    @SuppressLint("ValidFragment")
    private DefaultProgressDialog(Builder builder) {
        this();
        this.cancelable = builder.cancelable;
        this.canceledOnTouchOutside = builder.canceledOnTouchOutside;
        this.outsideBackground = builder.outsideBackground;
        this.showLoadingText = builder.showLoadingText;
        this.showLoadingTextContent = builder.showLoadingTextContent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Dialog attachDialog = getDialog();
        attachDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        attachDialog.setCancelable(this.cancelable);
        attachDialog.setCanceledOnTouchOutside(this.canceledOnTouchOutside);
        final Window attachWindow = attachDialog.getWindow();
        ViewGroup contentView = container;
        if (attachWindow != null) {
            contentView = attachWindow.findViewById(android.R.id.content);
            attachWindow.setBackgroundDrawable(new ColorDrawable(this.outsideBackground));
            attachWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        final View view = inflater.inflate(R.layout.dialog_net_loading_layout, contentView, false);
        TextView tvLoading = view.findViewById(R.id.tv_loading);
        tvLoading.setVisibility(this.showLoadingText ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(this.showLoadingTextContent)) {
            tvLoading.setText(this.showLoadingTextContent);
        }
        return view;
    }

    public static class Builder {
        private boolean cancelable;
        private boolean canceledOnTouchOutside;
        @ColorInt
        private int outsideBackground;
        private boolean showLoadingText;
        private String showLoadingTextContent;

        public Builder() {
            this.cancelable = false;
            this.canceledOnTouchOutside = false;
            this.outsideBackground = Color.TRANSPARENT;
            this.showLoadingText = true;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setOutsideBackground(@ColorInt int background) {
            this.outsideBackground = background;
            return this;
        }

        public Builder setShowLoadingText(boolean showLoadingText) {
            this.showLoadingText = showLoadingText;
            return this;
        }

        public Builder setShowLoadingTextContent(String textContent) {
            this.showLoadingTextContent = textContent;
            return this;
        }

        public DefaultProgressDialog build() {
            return new DefaultProgressDialog(this);
        }
    }
}
