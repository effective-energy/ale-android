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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.view.ButtonFont;
import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by dima on 2/21/18.
 */

public class RecoveryPhraseAskFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btConfirm;
    private TagsEditText edPhrase;
    private CheckBox ch1, ch2;

    private OnRecoveryPhraseAskDoneListener doneListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recovery_phase_ask, null);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        btConfirm = (ButtonFont) v.findViewById(R.id.bt_recovery_phrase_ask_continue);
        edPhrase = (TagsEditText) v.findViewById(R.id.tet_recovery_phase_ask_phrase);
        ch1 = (CheckBox) v.findViewById(R.id.chb_recovery_phrase_ask_1);
        ch2 = (CheckBox) v.findViewById(R.id.chb_recovery_phrase_ask_2);

        btConfirm.setOnClickListener(this);

        String[] els = new String[0];
        edPhrase.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, els));
        edPhrase.setThreshold(1);
        edPhrase.setTagsTextColor(R.color.text_dark);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enable();
            }
        });

        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enable();
            }
        });

        edPhrase.addTextChangedListener(new TextWatcher() {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enable();
    }

    private void enable() {
        if (ch1.isChecked() && ch2.isChecked() && edPhrase.getText().toString().length() > 0) {
            btConfirm.setEnabled(true);
        } else {
            btConfirm.setEnabled(false);
        }
    }


    public void setDoneListener(OnRecoveryPhraseAskDoneListener doneListener) {
        this.doneListener = doneListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_recovery_phrase_ask_continue) {
            if (doneListener != null) {
                doneListener.onRecoveryPhraseAskDone(edPhrase.getText().toString());
            }
        }
    }

    public interface OnRecoveryPhraseAskDoneListener {
        void onRecoveryPhraseAskDone(String recoveryPhrase);
    }
}
