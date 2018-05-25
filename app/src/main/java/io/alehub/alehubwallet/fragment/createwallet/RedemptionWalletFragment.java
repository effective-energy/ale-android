package io.alehub.alehubwallet.fragment.createwallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 2/21/18.
 */

public class RedemptionWalletFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText edName, edKey, edCert;
    private Button btRedemption;

    private OnRedemptionWalletNameDoneListener doneListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.redemption_wallet_fragment, null);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        edName = (EditText) v.findViewById(R.id.ed_wallet_redemption_name);
        edKey = (EditText) v.findViewById(R.id.ed_wallet_redemption_key);
        edCert = (EditText) v.findViewById(R.id.ed_wallet_redemption_phase_to_decript);
        btRedemption = (Button) v.findViewById(R.id.bt_wallet_redemption_redemption);

        btRedemption.setEnabled(false);
        btRedemption.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString("name", "");
            String key = savedInstanceState.getString("key", "");
            String cert = savedInstanceState.getString("cert", "");

            edName.setText(name);
            edKey.setText(key);
            edCert.setText(cert);
        }

        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enable();
            }
        });

        edKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enable();
            }
        });

        edCert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enable();
            }
        });

        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    private void enable() {
        String name = edName.getText().toString();
        String key = edKey.getText().toString();
        String cert = edCert.getText().toString();
        if (name.length() > 0 && key.length() > 0 && cert.length() > 0) {
            btRedemption.setEnabled(true);
        } else {
            btRedemption.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_wallet_redemption_redemption) {
            if (doneListener != null) {
                String name = edName.getText().toString();
                String key = edKey.getText().toString();
                String cert = edCert.getText().toString();
                doneListener.onRedemptionNameDone(name, key, cert);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String name = edName.getText().toString();
        String key = edKey.getText().toString();
        String cert = edCert.getText().toString();
        outState.putString("name", name);
        outState.putString("key", key);
        outState.putString("cert", cert);
    }

    public void setDoneListener(OnRedemptionWalletNameDoneListener doneListener) {
        this.doneListener = doneListener;
    }

    public interface OnRedemptionWalletNameDoneListener {
        void onRedemptionNameDone(String name, String key, String cert);
    }
}
