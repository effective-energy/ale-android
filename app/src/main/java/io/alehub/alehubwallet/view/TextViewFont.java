package io.alehub.alehubwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;

import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


import io.alehub.alehubwallet.App;
import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.util.FontUtil;

/**
 * Created by dima on 1/23/18.
 */

public class TextViewFont extends AppCompatTextView {


    public TextViewFont(Context context) {
        super(context);
    }

    public TextViewFont(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewFont);
        int font = a.getInt(R.styleable.TextViewFont_fontName, 0);
        FontUtil.setFont(this, font);
    }

    public TextViewFont(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewFont);
        int font = a.getInt(R.styleable.TextViewFont_fontName, 0);
        FontUtil.setFont(this, font);
    }



}
