package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.util.SettingsHelper;
import io.alehub.alehubwallet.view.ToolbarFont;

/**
 * Created by dima on 1/24/18.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText edFullName, edEmail, edPhone, edPassword;
    private ToolbarFont toolbar;
    private TextInputLayout tilPassword;
    private LinearLayout llPassword;
    private Spinner spLanguage, spTwofactorType;
    private SwitchCompat swTwoFactor;
    private TextView tvCodes;
    private Button btLogOut;

    private boolean twoFactor = true;

    public static boolean changeConfig = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, null);


        toolbar = (ToolbarFont) v.findViewById(R.id.toolbar);
        spLanguage = (Spinner) v.findViewById(R.id.sp_settings_language);
      /*  edEmail = (EditText) v.findViewById(R.id.ed_settings_email);
        edFullName = (EditText) v.findViewById(R.id.ed_settings_full_name);
        edPassword = (EditText) v.findViewById(R.id.ed_settings_password);
        edPhone = (EditText) v.findViewById(R.id.ed_settings_phone);

        spTwofactorType = (Spinner) v.findViewById(R.id.sp_settings_two_factor);
        swTwoFactor = (SwitchCompat) v.findViewById(R.id.sw_settings_two_factor);
        btLogOut = (Button) v.findViewById(R.id.bt_settings_log_out);
        tvCodes = (TextView) v.findViewById(R.id.tv_settings_show_codes);
        tilPassword = (TextInputLayout) v.findViewById(R.id.til_settings_password);
        llPassword = (LinearLayout) v.findViewById(R.id.ll_settings_password);*/

     /*   swTwoFactor.setChecked(twoFactor);
        swTwoFactor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spTwofactorType.setEnabled(isChecked);
                spTwofactorType.setFocusable(isChecked);
                twoFactor = isChecked;
            }
        });

        spTwofactorType.setEnabled(twoFactor);
        spTwofactorType.setFocusable(twoFactor);

        edPassword.setClickable(true);
        edPassword.setFocusable(false);
        tilPassword.setClickable(false);
        tilPassword.setFocusable(false);

        edPassword.setOnClickListener(this);
        btLogOut.setOnClickListener(this);
        tvCodes.setOnClickListener(this);*/

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (SettingsHelper.getLanguage(getActivity()).toLowerCase().equals("ru")) {
            spLanguage.setSelection(1);
        } else if (SettingsHelper.getLanguage(getActivity()).toLowerCase().equals("en")) {
            spLanguage.setSelection(0);
        }



        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        return v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if(!SettingsHelper.getLanguage(getActivity()).equals("en")) {
                        SettingsHelper.setLanguage(getActivity(), "en");
                        Log.e("RES", getString(R.string.add_wallet));
                        changeConfig = true;
                        getActivity().recreate();


                    }
                } else if (position == 1) {
                    if(!SettingsHelper.getLanguage(getActivity()).equals("ru")) {
                        SettingsHelper.setLanguage(getActivity(), "ru");
                        Log.e("RES", getString(R.string.add_wallet));
                        changeConfig = true;
                        getActivity().recreate();

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {
      /*  if (v.getId() == R.id.tv_settings_show_codes) {
            new BackupCodesFragment().show(getFragmentManager(), "backupcodes");
        } else if (v.getId() == R.id.bt_settings_log_out) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        } else if (v.getId() == R.id.ed_settings_password) {
            new ChangePasswordFragment().show(getFragmentManager(), "changepassword");
        }*/
    }

    public boolean isChangeConfig() {
        return changeConfig;
    }
}
