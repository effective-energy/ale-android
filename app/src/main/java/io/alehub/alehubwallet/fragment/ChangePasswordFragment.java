package io.alehub.alehubwallet.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.adapter.BackupCodesAdapter;
import io.alehub.alehubwallet.model.BackupCode;

/**
 * Created by dima on 1/24/18.
 */

public class ChangePasswordFragment extends DialogFragment implements View.OnClickListener {

    private EditText edOldPassword, edNewPassword, edRepeatPassword;
    private Button btChange;
    private ImageButton ibClose;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.change_password, null);

        btChange = (Button) v.findViewById(R.id.bt_change_password_change);
        ibClose = (ImageButton) v.findViewById(R.id.bt_change_password_close);

        edNewPassword = (EditText)v.findViewById(R.id.ed_change_password_new_password);
        edOldPassword = (EditText)v.findViewById(R.id.ed_change_password_old_password);
        edRepeatPassword = (EditText)v.findViewById(R.id.ed_change_password_new_password_repeat);

        btChange.setOnClickListener(this);
        ibClose.setOnClickListener(this);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return v;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = getResources().getDimensionPixelOffset(R.dimen.change_password_height);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_change_password_close) {
            getFragmentManager().beginTransaction().remove(this).commit();
        } else if (v.getId() == R.id.bt_change_password_change) {

        }
    }
}
