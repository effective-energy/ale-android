package io.alehub.alehubwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.util.FontUtil;

/**
 * Created by dima on 1/23/18.
 */

public class ButtonFont extends android.support.v7.widget.AppCompatButton {
    public ButtonFont(Context context) {
        super(context);
    }

    public ButtonFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ButtonFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ButtonFont);
        int font = a.getInt(R.styleable.ButtonFont_buttonFontName, 0);
        FontUtil.setFont(this, font);
        int leftIcon = a.getResourceId(R.styleable.ButtonFont_buttonLeftDrawable, 0);
        if (leftIcon > 0) {
            Drawable drawable = VectorDrawableCompat.create(getResources(), leftIcon, null);
          //  setCompoundDrawables(drawable, drawable, drawable, drawable);
            setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
        }
    }
}
