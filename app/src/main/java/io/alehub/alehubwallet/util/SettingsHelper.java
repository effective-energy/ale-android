package io.alehub.alehubwallet.util;

import android.content.Context;

/**
 * Created by dima on 1/30/18.
 */

public class SettingsHelper {

    private static final String NAME = "wallet_settings";

    private static final String VALUE_CODE = "code";
    private static final String VALUE_LANG = "language";


    public static String getCode(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                .getString(VALUE_CODE, "");
    }

    public static boolean setCode(Context context, String code) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(VALUE_CODE, code)
                .commit();
    }

    public static String getLanguage(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                .getString(VALUE_LANG, "en");
    }

    public static boolean setLanguage(Context context, String code) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(VALUE_LANG, code)
                .commit();
    }
}
