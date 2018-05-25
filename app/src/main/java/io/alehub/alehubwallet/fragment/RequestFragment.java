package io.alehub.alehubwallet.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.adapter.WalletsAdapter;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.network.GetWalletsHelper;
import io.alehub.alehubwallet.view.ButtonFont;
import io.alehub.alehubwallet.view.EditTextFont;
import io.alehub.alehubwallet.view.NotificationLayout;

/**
 * Created by dima on 2/6/18.
 */

public class RequestFragment extends Fragment implements View.OnClickListener {

    private EditTextFont edAddress;
    private ButtonFont btShare, btCopy;
    private ImageView ivQrCode;
    private NotificationLayout nlNotify;
    private Spinner spWallets;

    private List<Wallet> wallets = new ArrayList<>();

    private String code;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.request_fragment, null);

        btShare = (ButtonFont) v.findViewById(R.id.bt_wallet_request_share);
        btCopy = (ButtonFont) v.findViewById(R.id.bt_wallet_request_copy);
        ivQrCode = (ImageView) v.findViewById(R.id.iv_wallet_request_qrcode);
        edAddress = (EditTextFont) v.findViewById(R.id.ed_wallet_requestr_address);
        nlNotify = (NotificationLayout) v.findViewById(R.id.nl_request_notify);
        spWallets = (Spinner) v.findViewById(R.id.sp_request_wallet);

        btShare.setOnClickListener(this);
        btCopy.setOnClickListener(this);

        edAddress.setFocusable(false);
        edAddress.setClickable(false);

        spWallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                code = wallets.get(position).getPublicKey();
                initWallet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetWalletsHelper(getActivity(), new GetWalletsHelper.OnGetWalletsListener() {
            @Override
            public void onGetWallets(List<Wallet> wallets) {
                RequestFragment.this.wallets = wallets;
                spWallets.setAdapter(new WalletsAdapter(getActivity(), wallets));
                if (wallets.size() > 0) {
                    code = wallets.get(0).getPublicKey();
                    initWallet();
                }
            }
        }).execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("code", code);
    }

    private void initWallet() {
        edAddress.setText(code);
        Bitmap myBitmap = QRCode.from(code).withSize(500, 500).bitmap();
        ivQrCode.setImageBitmap(myBitmap);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_wallet_request_close) {
            getFragmentManager().beginTransaction().remove(this).commit();
        } else if (v.getId() == R.id.bt_wallet_request_copy) {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Share code", code);
            clipboard.setPrimaryClip(clip);
            nlNotify.showCompleate(R.string.qrcode_copyd_success);
        } else if (v.getId() == R.id.bt_wallet_request_share) {
            String shareBody = code;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share code");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
            //nlNotify.showCompleate(R.string.qrcode_shared_success);
        }
    }
}