package io.alehub.alehubwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 1/23/18.
 */

public class ImageButtonVector extends android.support.v7.widget.AppCompatImageButton {
    public ImageButtonVector(Context context) {
        super(context);
    }

    public ImageButtonVector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ImageButtonVector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageButtonVector);

        int leftIcon = a.getResourceId(R.styleable.ImageButtonVector_imageDrawable, 0);
        if (leftIcon > 0) {
            Drawable drawable = VectorDrawableCompat.create(getResources(), leftIcon, null);
            //  setCompoundDrawables(drawable, drawable, drawable, drawable);
            setImageDrawable(drawable);
        }
    }
}
