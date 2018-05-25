package io.alehub.alehubwallet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import io.alehub.alehubwallet.util.LocaleManager;
import io.alehub.alehubwallet.util.SettingsHelper;

/**
 * Created by dima on 24.03.18.
 */

public class LangBaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//FromSourcesEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LocaleManager.setLanguage(newBase, SettingsHelper.getLanguage(newBase));
        super.attachBaseContext(context);
    }
}
