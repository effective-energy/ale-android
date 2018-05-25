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
import android.widget.TextView;

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 2/21/18.
 */

public class RecoveryPhraseShowFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btContinue;
    private TextView tvPhrase;

    private OnShowPhraseDoneListener doneListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recovery_phase_show, null);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        btContinue = (Button) v.findViewById(R.id.bt_recovery_phrase_show_continue);
        tvPhrase = (TextView) v.findViewById(R.id.tv_recovery_phrase_show_phrase);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btContinue.setOnClickListener(this);

        if (getArguments() != null) {
            tvPhrase.setText(getArguments().getString("phrase", ""));
        }

        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }


    public void setDoneListener(OnShowPhraseDoneListener doneListener) {
        this.doneListener = doneListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_recovery_phrase_show_continue) {
            if (doneListener != null) {
                doneListener.onShowPhraseDone();
            }
        }
    }

    public interface OnShowPhraseDoneListener {
        void onShowPhraseDone();
    }
}
