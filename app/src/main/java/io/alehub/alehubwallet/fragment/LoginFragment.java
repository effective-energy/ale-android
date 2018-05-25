package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;

import io.alehub.alehubwallet.LoginActivity;
import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.view.NotificationLayout;

/**
 * Created by dima on 1/23/18.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {


    private EditText edLogin, edPassword;
    private Toolbar toolbar;
    private Button btLogin;
    private TextView tvCreate;
    private NotificationLayout nlNotify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, null);

        edLogin = (EditText) v.findViewById(R.id.ed_login_email);
        edPassword = (EditText) v.findViewById(R.id.ed_login_password);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        btLogin = (Button) v.findViewById(R.id.bt_login_login);
        tvCreate = (TextView) v.findViewById(R.id.tv_login_create_one);
        nlNotify = (NotificationLayout) v.findViewById(R.id.notification);

        btLogin.setOnClickListener(this);
        tvCreate.setOnClickListener(this);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        return v;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.bt_login_login) {
            String login = edLogin.getText().toString();
            String password = edPassword.getText().toString();
            if (login.length() < 4 || !EmailValidator.getInstance().isValid(login)) {
                nlNotify.show(getString(R.string.email_incorrect), R.color.text_white, R.color.red);
            } else if (password.length() < 6) {
                nlNotify.show(getString(R.string.password_incorrect), R.color.text_white, R.color.red);
            } else {
                if (getActivity() instanceof LoginActivity) {
                    ((LoginActivity) getActivity()).showPinCodeFragment();
                }
            }
        } else if (view.getId() == R.id.tv_login_create_one) {
            if (getActivity() instanceof LoginActivity) {
                ((LoginActivity) getActivity()).showRegistration();
            }
        }
    }
}
