package io.alehub.alehubwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 1/24/18.
 */

public class NotificationLayout extends LinearLayout {

    private TextView tvText;
    private LinearLayout llBg;

    long showTime = 0;


    public NotificationLayout(Context context) {
        super(context);
        inflateLL(context);
    }

    public NotificationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateLL(context);
        initAttrs(context, attrs);
    }

    public NotificationLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateLL(context);
        initAttrs(context, attrs);
    }

    private void inflateLL(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_layout, null);
        tvText = (TextView) view.findViewById(R.id.tv_notification_text);
        llBg = (LinearLayout) view.findViewById(R.id.ll_notification_bg);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        addView(view);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NotificationLayout);
            int color = a.getResourceId(R.styleable.NotificationLayout_notificationTextColor, 0);
            if (color > 0) {
                setTextColor(color);
            }
            String text = a.getString(R.styleable.NotificationLayout_notificationText);
            if (text != null) {
                setText(text);
            }
            int bg = a.getResourceId(R.styleable.NotificationLayout_notificationBg, 0);
            if (bg > 0) {
                setBackgroundResource(bg);
            }
        }
        setVisibility(INVISIBLE);
    }

    private Runnable hide = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - showTime >= 5000) {
                setVisibility(INVISIBLE);
            }
        }
    };

    public void show(String text, int textColor, int bg) {
        setText(text);
        setTextColor(textColor);
        setBackgroundResource(bg);
        show();
    }

    public void showError(String text) {
        show(text, R.color.text_white, R.color.red);
    }

    public void showError(int text) {
        show(getContext().getString(text), R.color.text_white, R.color.red);
    }

    public void showCompleate(int text) {
        show(getContext().getString(text), R.color.text_white, R.color.green);
    }

    public void showCompleate(String text) {
        show(text, R.color.text_white, R.color.green);
    }


    public void show() {
        setVisibility(VISIBLE);
        showTime = System.currentTimeMillis();
        postDelayed(hide, 5010);
    }

    private void hide() {
        showTime = 0;
        post(hide);
    }

    public void setText(String text) {
        tvText.setText(text);
    }

    public void setText(int id) {
        tvText.setText(id);
    }

    public void setTextColor(int id) {
        tvText.setTextColor(getContext().getResources().getColor(id));
    }

    @Override
    public void setBackground(Drawable background) {
        llBg.setBackground(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        llBg.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        llBg.setBackgroundResource(resid);
    }
}
