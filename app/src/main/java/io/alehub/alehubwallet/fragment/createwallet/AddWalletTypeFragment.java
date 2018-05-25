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

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 2/21/18.
 */

public class AddWalletTypeFragment extends Fragment implements View.OnClickListener {

    private Button btAdd, btImport, btRestore;
    private Toolbar toolbar;

    public final static int TYPE_NEW = 1;
    public final static int TYPE_IMPORT = 2;
    public final static int TYPE_REDEMPTION = 3;

    private OnTypeSelectListener typeSelectListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_wallet_type_fragment, null);

        btAdd = (Button) v.findViewById(R.id.bt_add_wallet_create_new);
        btImport = (Button) v.findViewById(R.id.bt_add_wallet_import);
        btRestore = (Button) v.findViewById(R.id.bt_add_wallet_restore);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btRestore.setOnClickListener(this);
        btImport.setOnClickListener(this);
        btAdd.setOnClickListener(this);

        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    public void setTypeSelectListener(OnTypeSelectListener typeSelectListener) {
        this.typeSelectListener = typeSelectListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_add_wallet_create_new) {
            if(typeSelectListener!= null){
                typeSelectListener.onTypeSelect(TYPE_NEW);
            }
        } else if (v.getId() == R.id.bt_add_wallet_import) {
            if(typeSelectListener!= null){
                typeSelectListener.onTypeSelect(TYPE_IMPORT);
            }
        } else if (v.getId() == R.id.bt_add_wallet_restore) {
            if(typeSelectListener!= null){
                typeSelectListener.onTypeSelect(TYPE_REDEMPTION);
            }
        }
    }

    public interface OnTypeSelectListener {
        void onTypeSelect(int type);
    }
}
