package io.alehub.alehubwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.util.FontUtil;

/**
 * Created by dima on 1/23/18.
 */

public class EditTextFont extends AppCompatEditText {
    public EditTextFont(Context context) {
        super(context);
    }

    public EditTextFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextFont);
        int font = a.getInt(R.styleable.EditTextFont_editTextFontName, 0);
        FontUtil.setFont(this, font);
    }

    public EditTextFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextFont);
        int font = a.getInt(R.styleable.EditTextFont_editTextFontName, 0);
        FontUtil.setFont(this, font);
    }
}
