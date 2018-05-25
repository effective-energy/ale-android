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

public class CreateWalletFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText edName;
    private Button btCreate;

    private OnCreateWalletNameDoneListener doneListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_wallet_fragment, null);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        edName = (EditText) v.findViewById(R.id.ed_create_wallet_name);
        btCreate = (Button) v.findViewById(R.id.bt_create_wallet_create);

        btCreate.setEnabled(false);
        btCreate.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString("name", "");

            edName.setText(name);
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


        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String name = edName.getText().toString();
        outState.putString("name", name);
    }


    private void enable() {
        String name = edName.getText().toString();
        if (name.length() > 0) {
            btCreate.setEnabled(true);
        } else {
            btCreate.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_create_wallet_create) {
            if (doneListener != null) {
                String name = edName.getText().toString();
                doneListener.onCreateNameDone(name);
            }
        }
    }

    public void setDoneListener(OnCreateWalletNameDoneListener doneListener) {
        this.doneListener = doneListener;
    }

    public interface OnCreateWalletNameDoneListener {
        void onCreateNameDone(String name);
    }
}
