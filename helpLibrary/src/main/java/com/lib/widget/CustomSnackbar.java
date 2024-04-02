package com.lib.widget;

import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.lib.R;

public class CustomSnackbar {

    private Snackbar snackbar;

    private String messageText;
    private String actionText;
    private int messageTextColor;
    private int actionTextColor;
    private int duration;

    private View layoutContainerView;
    private View.OnClickListener onActionClickListener;

    private CustomSnackbar(Builder builder) {
        messageText = builder.messageText;
        actionText = builder.actionText;
        messageTextColor = builder.messageTextColor;
        actionTextColor = builder.actionTextColor;
        duration = builder.duration;
        layoutContainerView = builder.layoutContainerView;
        onActionClickListener = builder.onActionClickListener;
    }

    public void showSnackbar() {
        if (layoutContainerView != null) {
            snackbar = Snackbar.make(layoutContainerView, messageText, duration);
            if (onActionClickListener != null) {
                snackbar.setAction(actionText, onActionClickListener);
                snackbar.setActionTextColor(ContextCompat.getColor(layoutContainerView.getContext(), actionTextColor));
            }
            View viewSnackbar = snackbar.getView();
            viewSnackbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            TextView tvSnackbar = viewSnackbar.findViewById(R.id.snackbar_text);
            if (tvSnackbar != null) {
                tvSnackbar.setTextColor(ContextCompat.getColor(layoutContainerView.getContext(), messageTextColor));
            }
            snackbar.show();
        }
    }

    public void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public static final class Builder {

        private String messageText = "";
        private String actionText = "";
        private int messageTextColor = android.R.color.white;
        private int actionTextColor = R.color.color_red_0500;
        private int duration = Snackbar.LENGTH_SHORT;
        private View layoutContainerView;
        private View.OnClickListener onActionClickListener;

        public Builder() {
        }

        public Builder messageText(String val) {
            messageText = val;
            return this;
        }

        public Builder actionText(String val) {
            actionText = val;
            return this;
        }

        public Builder messageTextColor(int val) {
            messageTextColor = val;
            return this;
        }

        public Builder actionTextColor(int val) {
            actionTextColor = val;
            return this;
        }

        public Builder duration(int val) {
            duration = val;
            return this;
        }

        public Builder layoutContainerView(View val) {
            layoutContainerView = val;
            return this;
        }

        public Builder onActionClickListener(View.OnClickListener val) {
            onActionClickListener = val;
            return this;
        }

        public CustomSnackbar build() {
            return new CustomSnackbar(this);
        }

    }

}