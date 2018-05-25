package io.alehub.alehubwallet.fragment.createwallet;

import android.app.Activity;
import android.content.Intent;
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

import io.alehub.alehubwallet.QRCodeScanActivity;
import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.view.ButtonFont;
import io.alehub.alehubwallet.view.EditTextFont;
import io.alehub.alehubwallet.view.ImageButtonVector;

/**
 * Created by dima on 2/21/18.
 */

public class ImportWalletFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText edName, edSecretKey;
    private Button btImport;
    private ImageButtonVector ibQrcode;

    private OnImportWalletNameDoneListener doneListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.import_wallet_fragment, null);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        edName = (EditTextFont) v.findViewById(R.id.ed_import_wallet_name);
        edSecretKey = (EditTextFont) v.findViewById(R.id.ed_import_wallet_secret_key);
        btImport = (ButtonFont) v.findViewById(R.id.bt_import_wallet_import);
        ibQrcode = (ImageButtonVector) v.findViewById(R.id.ib_import_wallet_scan_code);

        btImport.setEnabled(false);
        btImport.setOnClickListener(this);
        ibQrcode.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString("name", "");
            String key = savedInstanceState.getString("key", "");

            edName.setText(name);
            edSecretKey.setText(key);
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

        edSecretKey.addTextChangedListener(new TextWatcher() {
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
        String key = edSecretKey.getText().toString();
        if (name.length() > 0 && key.length() > 0) {
            btImport.setEnabled(true);
        } else {
            btImport.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        String name = edName.getText().toString();
        String key = edSecretKey.getText().toString();
        if (v.getId() == R.id.bt_import_wallet_import) {
            if (doneListener != null) {
                doneListener.onImportNameDone(name, key);
            }
        } else if (v.getId() == R.id.ib_import_wallet_scan_code) {
            startActivityForResult(new Intent(getActivity(), QRCodeScanActivity.class), 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String str = data.getStringExtra("data");
            edSecretKey.setText(str);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setDoneListener(OnImportWalletNameDoneListener doneListener) {
        this.doneListener = doneListener;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String name = edName.getText().toString();
        String key = edSecretKey.getText().toString();
        outState.putString("name", name);
        outState.putString("key", key);
    }

    public interface OnImportWalletNameDoneListener {
        void onImportNameDone(String name, String secretKey);
    }


}
