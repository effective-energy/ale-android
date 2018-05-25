package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.listener.WalletListener;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.view.ButtonFont;
import io.alehub.alehubwallet.view.EditTextFont;
import io.alehub.alehubwallet.view.TextViewFont;

/**
 * Created by dima on 2/6/18.
 */

public class WalletSettingFragment extends Fragment implements View.OnClickListener {

    private EditText edName;
    private TextView tvDelete;
    private WalletListener listener;
    private Button btSave;
    private Wallet wallet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wallet_settings_fragment, null);

        edName = (EditTextFont) v.findViewById(R.id.ed_wallet_settings_name);
        tvDelete = (TextViewFont) v.findViewById(R.id.tv_wallet_settings_delete);
        btSave = (ButtonFont) v.findViewById(R.id.bt_wallet_settings_save_name);

        tvDelete.setOnClickListener(this);
        btSave.setOnClickListener(this);


        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    public void setListener(WalletListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            wallet = (Wallet) savedInstanceState.getSerializable("wallet");
        } else if (getArguments() != null) {
            wallet = (Wallet) getArguments().getSerializable("wallet");
        }
        edName.setText(wallet.getName());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("wallet", wallet);
    }

    private void delete() {
        DeleteWalletFragment dl = new DeleteWalletFragment();
        dl.setListener(new DeleteWalletFragment.OnDeleteWalletListener() {
            @Override
            public void onDelete() {
                if (listener != null) {
                    listener.onDeleteWallet(wallet);
                }
            }
        });
        dl.show(getChildFragmentManager(), "deletefragment");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_wallet_settings_delete) {
            delete();
        } else if (v.getId() == R.id.bt_wallet_settings_save_name) {
            String name = edName.getText().toString();
            if (name.length() > 0) {
                wallet.setName(edName.getText().toString());
                if (listener != null) {
                    listener.updateWallet(wallet);
                }
            } else {
                if (listener != null) {
                    listener.showNotification(WalletListener.TYPE_ERROR, R.string.name_zero);
                }
            }
        }
    }
}
