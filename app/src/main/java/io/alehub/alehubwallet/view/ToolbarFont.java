package io.alehub.alehubwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.util.FontUtil;

/**
 * Created by dima on 1/23/18.
 */

public class ToolbarFont extends Toolbar {
    public ToolbarFont(Context context) {
        super(context);
    }

    public ToolbarFont(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ToolbarFont(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ToolbarFont);

          /*  Drawable icon = a.getDrawable(R.styleable.ToolbarFont_toolbarBackIcon);
            setNavigationIcon(icon);/=*/
            int icon = a.getResourceId(R.styleable.ToolbarFont_toolbarBackIcon, 0);
            if (icon > 0) {
                Drawable drawable = VectorDrawableCompat.create(getResources(), icon, null);
                setNavigationIcon(drawable);
            }

            int font = a.getInt(R.styleable.ToolbarFont_toolbarFont, 1);
            setFont(this, font);
        }
    }


    private void setFont(ViewGroup v, int font) {
        for (int i = 0; i < v.getChildCount(); i++) {
            if (v.getChildAt(i) instanceof ViewGroup) {
                setFont((ViewGroup) v.getChildAt(i), font);
            } else if (v.getChildAt(i) instanceof TextView) {
                FontUtil.setFont((TextView) v.getChildAt(i), font);
            }
        }
    }
}
