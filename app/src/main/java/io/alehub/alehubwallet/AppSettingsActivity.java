package io.alehub.alehubwallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.alehub.alehubwallet.fragment.SettingsFragment;

/**
 * Created by dima on 24.03.18.
 */

public class AppSettingsActivity extends LangBaseActivity {

    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, settingsFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (settingsFragment.isChangeConfig()) {
            setResult(Activity.RESULT_OK);
            finish();
        }else{
            finish();
        }
    }
}
