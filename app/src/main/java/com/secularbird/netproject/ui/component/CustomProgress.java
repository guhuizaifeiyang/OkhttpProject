package com.secularbird.netproject.ui.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.secularbird.netproject.R;


public class CustomProgress extends Dialog {

    public CustomProgress(Context context) {
        super(context);
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView
                .getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView text = (TextView) findViewById(R.id.message);
            text.setText(message);
            text.invalidate();
        }
    }

    public static CustomProgress show(Context context, CharSequence message,
                                      boolean cancelable, OnCancelListener cancelListener) {
        CustomProgress dialog = new CustomProgress(context,
                R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_custom);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView text = (TextView) dialog.findViewById(R.id.message);
            text.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return dialog;
    }

}
