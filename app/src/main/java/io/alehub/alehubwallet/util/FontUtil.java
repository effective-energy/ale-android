package io.alehub.alehubwallet.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import io.alehub.alehubwallet.App;

/**
 * Created by dima on 1/23/18.
 */

public class FontUtil {

    private static Typeface FONT_FAIRVIEW, FONT_MUSEO_300, FONT_MUSEO_500, FONT_MUSEO_700;

    private static String getFontNameByInt(int f) {
        if (f == 1) {
            return "fairview";
        } else if (f == 2) {
            return "museo_300";
        } else if (f == 3) {
            return "museo_500";
        } else if (f == 4) {
            return "museo_700";
        } else {
            return null;
        }
    }

    private static void initFonts(Context context) {
        FONT_FAIRVIEW = Typeface.createFromAsset(context.getAssets(), "fonts/fairview.otf");
        FONT_MUSEO_300 = Typeface.createFromAsset(context.getAssets(), "fonts/museo_300.ttf");
        FONT_MUSEO_500 = Typeface.createFromAsset(context.getAssets(), "fonts/museo_500.ttf");
        FONT_MUSEO_700 = Typeface.createFromAsset(context.getAssets(), "fonts/museo_700.ttf");
    }

    public static Typeface getFont(Context context, String name) {
        if (name.equals("fairview")) {
            if (FONT_FAIRVIEW == null) {
                initFonts(context);
            }
            return FONT_FAIRVIEW;
        } else if (name.equals("museo_300")) {
            if (FONT_FAIRVIEW == null) {
                initFonts(context);
            }
            return FONT_MUSEO_300;
        } else if (name.equals("museo_500")) {
            if (FONT_FAIRVIEW == null) {
                initFonts(context);
            }
            return FONT_MUSEO_500;
        } else if (name.equals("museo_700")) {
            if (FONT_FAIRVIEW == null) {
                initFonts(context);
            }
            return FONT_MUSEO_700;
        }else{
            return Typeface.DEFAULT;
        }
    }

    public static void setFont(TextView view, int fontId) {
        String name = getFontNameByInt(fontId);
        setFont(view, name);
    }

    public static void setFont(TextView view, String name) {
        if (name != null) {
            view.setTypeface(getFont(view.getContext(), name));
        }
    }

}
