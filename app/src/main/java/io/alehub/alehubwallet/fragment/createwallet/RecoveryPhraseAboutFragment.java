package io.alehub.alehubwallet.fragment.createwallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 2/21/18.
 */

public class RecoveryPhraseAboutFragment extends Fragment implements View.OnClickListener {

    private Button btContinue;
    private CheckBox chbConfirm;
    private Toolbar toolbar;

    private int continueTime = 0;

    private OnAboutConfirmListener confirmListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recovery_phase_about, null);


        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        btContinue = (Button) v.findViewById(R.id.bt_recovery_phrase_about_continue);
        chbConfirm = (CheckBox) v.findViewById(R.id.chb_recovery_phrase_bout_accept);

        chbConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enable();
            }
        });

        btContinue.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btContinue.setEnabled(false);
        btContinue.setFocusable(false);
        continueTime = 10;
        btContinue.setText(getString(R.string.continue_text) + " (" + continueTime + ")");
        btContinue.postDelayed(rContinue, 1000);
    }

    private void enable() {
        if (continueTime < 1) {
            if (chbConfirm.isChecked()) {
                btContinue.setEnabled(true);
            } else {
                btContinue.setEnabled(false);
            }
        }
    }

    public void setConfirmListener(OnAboutConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    private Runnable rContinue = new Runnable() {
        @Override
        public void run() {
            continueTime--;
            if (continueTime < 1) {
                btContinue.setText(R.string.continue_text);
                if (chbConfirm.isChecked()) {
                    btContinue.setEnabled(true);
                    btContinue.setFocusable(true);
                }
            } else {
                try {
                    btContinue.postDelayed(this, 1000);
                    btContinue.setText(getString(R.string.continue_text) + " (" + continueTime + ")");
                } catch (Exception e) {

                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_recovery_phrase_about_continue) {
            if (confirmListener != null) {
                confirmListener.onAboutConfirm();
            }
        }
    }

    public interface OnAboutConfirmListener {
        void onAboutConfirm();
    }
}
