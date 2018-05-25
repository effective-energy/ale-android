package io.alehub.alehubwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import io.alehub.alehubwallet.fragment.InitFragment;
import io.alehub.alehubwallet.fragment.LoginFragment;
import io.alehub.alehubwallet.fragment.PinCodeFragment;
import io.alehub.alehubwallet.fragment.RegistrationFragment;
import io.alehub.alehubwallet.util.SettingsHelper;

/**
 * Created by dima on 1/23/18.
 */

public class LoginActivity extends LangBaseActivity implements InitFragment.OnInitDoneListener {



    public static final String LOGIN_FRAGMENT_TAG = "loginfragment";
    public static final String REGISTRATION_FRAGMENT_TAG = "registrationfragment";
    public static final String PIN_CODE_FRAGMENT_TAG = "pinncodefragment";

    private LoginFragment loginFragment;
    private RegistrationFragment registrationFragment;
    private PinCodeFragment pincodeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        if (savedInstanceState == null) {
            showInitFragment();
        }
    }

    public void showInitFragment() {
        InitFragment fr = new InitFragment();
        fr.setDoneListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fr)
                .addToBackStack("init")
                .commit();
    }

    public void showRegistration() {
        if (registrationFragment == null) {
            registrationFragment = new RegistrationFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, registrationFragment)
                .addToBackStack(REGISTRATION_FRAGMENT_TAG)
                .commit();
    }

    public void showLoginFragment() {

        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, loginFragment)
                .addToBackStack(LOGIN_FRAGMENT_TAG)
                .commit();
        goToMain();
    }

    public void showPinCodeFragment() {
        if (pincodeFragment == null) {
            pincodeFragment = new PinCodeFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, pincodeFragment)
                .addToBackStack(PIN_CODE_FRAGMENT_TAG)
                .commit();

    }

    public void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            try {
                System.gc();
            } catch (Exception e) {
            }
            FragmentManager fsm = getSupportFragmentManager();
            FragmentTransaction ftransaction = fsm.beginTransaction();
            fsm.popBackStack();
            ftransaction.commit();

        } else {
            finish();
        }
    }

    @Override
    public void onInitDone() {
        if (SettingsHelper.getCode(this).length() > 0) {
            goToMain();
        } else {
            showLoginFragment();
        }
    }
}
