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

public class BackupCodesFragment extends DialogFragment implements View.OnClickListener {

    private RecyclerView rvCodes;
    private Button btGenerateNew;
    private ImageButton ibClose;

    private BackupCodesAdapter adapter;

    private List<BackupCode> codes = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.backup_codes, null);

        rvCodes = (RecyclerView) v.findViewById(R.id.rv_bacup_codes_list);
        btGenerateNew = (Button) v.findViewById(R.id.bt_backup_codes_generate_new);
        ibClose = (ImageButton) v.findViewById(R.id.bt_backup_codes_close);

        generateCodes();

        rvCodes.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BackupCodesAdapter(getActivity());
        rvCodes.setAdapter(adapter);


        for (BackupCode code : codes) {
            adapter.addElement(code);
        }

        btGenerateNew.setOnClickListener(this);
        ibClose.setOnClickListener(this);


        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        return v;
    }

    private void generateCodes() {
        codes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BackupCode bc = new BackupCode(generateCode(), new Random().nextBoolean(), new Random().nextLong());
            codes.add(bc);
        }
    }

    private String generateCode() {
        char[] ch = {'1', '2', '3', '4', '5', '6', '7', '8', '9',
                '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o',
                'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
                'z', 'x', 'c', 'v', 'b', 'n', 'm'};
        String str = "";
        for (int i = 0; i < 10; i++) {
            str += ch[new Random().nextInt(ch.length)];
        }
        return str;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void generateNewCode() {
        int usedPos = -1;
        for (int i = 0; i < codes.size(); i++) {
            if (codes.get(i).isUsed()) {
                usedPos = i;
                break;
            }
        }
        if (usedPos > -1) {
            BackupCode code = new BackupCode(generateCode(), false, -1);
            adapter.setElement(usedPos, code);
            codes.set(usedPos,code);
        } else {
            for(int i = 0;i< 10; i++){
                BackupCode code = new BackupCode(generateCode(), false, -1);
                adapter.setElement(i,code);
                codes.set(i,code);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_backup_codes_close) {
            getFragmentManager().beginTransaction().remove(this).commit();
        } else if (v.getId() == R.id.bt_backup_codes_generate_new) {
            generateNewCode();
        }
    }
}
