package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.view.NotificationLayout;

/**
 * Created by dima on 1/23/18.
 */

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private EditText edFullName, edEmail, edPhoneNumber, edPassword, edRetypePassword;
    private Button btCreate;
    private TextView tvLogIn;
    private Toolbar toolbar;
    private NotificationLayout nlNotify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_fragment, null);

        edEmail = (EditText) v.findViewById(R.id.ed_registration_email);
        edFullName = (EditText) v.findViewById(R.id.ed_registration_full_name);
        edPassword = (EditText) v.findViewById(R.id.ed_registration_password);
        edPhoneNumber = (EditText) v.findViewById(R.id.ed_registration_phone_number);
        edRetypePassword = (EditText) v.findViewById(R.id.ed_registration_retype_password);
        tvLogIn = (TextView) v.findViewById(R.id.tv_registration_log_in);
        btCreate = (Button) v.findViewById(R.id.bt_registration_create);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        nlNotify = (NotificationLayout) v.findViewById(R.id.notification);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btCreate.setOnClickListener(this);
        tvLogIn.setOnClickListener(this);


        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        return v;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_registration_create) {
            String fullName = edFullName.getText().toString();
            String email = edEmail.getText().toString();
            String phone = edPhoneNumber.getText().toString();
            String password = edPassword.getText().toString();
            String password1 = edRetypePassword.getText().toString();
            if (fullName.length() < 1) {
                nlNotify.show(getString(R.string.fullname_incorrect), R.color.text_white, R.color.red);
            } else if (email.length() < 4 || !EmailValidator.getInstance().isValid(email)) {
                nlNotify.show(getString(R.string.email_incorrect), R.color.text_white, R.color.red);
            } else if (phone.length() < 1) {
                nlNotify.show(getString(R.string.phone_incorrect), R.color.text_white, R.color.red);
            } else if (password.length() < 6 || !password.equals(password1)) {
                nlNotify.show(getString(R.string.password_incorrect), R.color.text_white, R.color.red);
            } else {
                getActivity().onBackPressed();
            }
        } else if (view.getId() == R.id.tv_registration_log_in) {
            getActivity().onBackPressed();
        }
    }
}
