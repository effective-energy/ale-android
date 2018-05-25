package io.alehub.alehubwallet.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 1/24/18.
 */

public class DeleteWalletFragment extends DialogFragment implements View.OnClickListener {

    private CheckBox chAccept;
    private Button btDelete, btCancel;
    private ImageButton ibClose;

    private int continueTime = 10;
    private boolean chk = false;

    private OnDeleteWalletListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.delete_wallet, null);

        btDelete = (Button) v.findViewById(R.id.bt_wallet_delete_delete);
        ibClose = (ImageButton) v.findViewById(R.id.bt_wallet_delete_close);
        btCancel = (Button) v.findViewById(R.id.bt_wallet_delete_cancel);
        chAccept = (CheckBox) v.findViewById(R.id.chb_wallet_delete_accept);


        btCancel.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        ibClose.setOnClickListener(this);

        chAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chk = isChecked;
                if (chk && continueTime < 1) {
                    btDelete.setEnabled(true);
                    btDelete.setFocusable(true);
                } else {
                    btDelete.setEnabled(false);
                    btDelete.setFocusable(false);
                }
            }
        });

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return v;
    }

    public void setListener(OnDeleteWalletListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btDelete.setEnabled(false);
        btDelete.setFocusable(false);
        continueTime = 10;
        btDelete.setText(getString(R.string.delete) + " (" + continueTime + ")");
        btDelete.postDelayed(rContinue, 1000);
    }

    private Runnable rContinue = new Runnable() {
        @Override
        public void run() {
            continueTime--;
            if (continueTime < 1) {
                btDelete.setText(R.string.delete);
                if (chk) {
                    btDelete.setEnabled(true);
                    btDelete.setFocusable(true);
                }
            } else {
                try {
                    btDelete.postDelayed(this, 1000);
                    btDelete.setText(getString(R.string.delete) + " (" + continueTime + ")");
                } catch (Exception e) {

                }
            }
        }
    };

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
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_wallet_delete_close || v.getId() == R.id.bt_wallet_delete_cancel) {
            getFragmentManager().beginTransaction().remove(this).commit();
        } else if (v.getId() == R.id.bt_wallet_delete_delete) {
            if (listener != null) {
                listener.onDelete();
            }
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public interface OnDeleteWalletListener {
        void onDelete();
    }
}
