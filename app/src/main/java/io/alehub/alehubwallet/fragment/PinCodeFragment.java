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


import io.alehub.alehubwallet.LoginActivity;
import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.view.NotificationLayout;

/**
 * Created by dima on 1/23/18.
 */

public class PinCodeFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private NotificationLayout nlNotify;
    private EditText edCode;
    private Button btBack, btConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.code_fragment, null);

        toolbar = (android.support.v7.widget.Toolbar) v.findViewById(R.id.toolbar);
        nlNotify = (NotificationLayout) v.findViewById(R.id.notification);
        btBack = (Button) v.findViewById(R.id.bt_code_back);
        btConfirm = (Button) v.findViewById(R.id.bt_code_confirm);
        edCode = (EditText) v.findViewById(R.id.ed_code_code);

        btConfirm.setOnClickListener(this);
        btBack.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_code_back) {
            getActivity().onBackPressed();
        } else if (v.getId() == R.id.bt_code_confirm) {
            String code = edCode.getText().toString();
            if (code.length() < 4) {
                nlNotify.showError(R.string.code_incorrect);
            } else {
                if (getActivity() instanceof LoginActivity) {
                    ((LoginActivity) getActivity()).goToMain();
                }
            }
        }
    }
}
