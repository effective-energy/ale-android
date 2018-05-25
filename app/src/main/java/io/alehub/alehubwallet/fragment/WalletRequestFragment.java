package io.alehub.alehubwallet.fragment;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.view.ButtonFont;
import io.alehub.alehubwallet.view.EditTextFont;

/**
 * Created by dima on 2/6/18.
 */

public class WalletRequestFragment extends DialogFragment implements View.OnClickListener {

    private EditText edAddress;
    private Button btShare, btCopy;
    private ImageButton ibClose;
    private ImageView ivQrCode;

    private String code;
    private OnRequestDoneListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wallet_request, null);

        btShare = (ButtonFont) v.findViewById(R.id.bt_wallet_request_share);
        btCopy = (ButtonFont) v.findViewById(R.id.bt_wallet_request_copy);
        ibClose = (ImageButton) v.findViewById(R.id.bt_wallet_request_close);
        ivQrCode = (ImageView) v.findViewById(R.id.iv_wallet_request_qrcode);
        edAddress = (EditTextFont) v.findViewById(R.id.ed_wallet_requestr_address);

        if (savedInstanceState != null) {
            code = savedInstanceState.getString("code", "");
        } else {
            code = getArguments().getString("code", "");
        }


        btShare.setOnClickListener(this);
        btCopy.setOnClickListener(this);
        ibClose.setOnClickListener(this);

        edAddress.setFocusable(false);
        edAddress.setClickable(false);
        edAddress.setText(code);



        Bitmap myBitmap = QRCode.from(code).withSize(500, 500).bitmap();
        ivQrCode.setImageBitmap(myBitmap);


        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("code", code);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setListener(OnRequestDoneListener listener) {
        this.listener = listener;
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
        if (v.getId() == R.id.bt_wallet_request_close) {
            getFragmentManager().beginTransaction().remove(this).commit();
        } else if (v.getId() == R.id.bt_wallet_request_copy) {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Share code", code);
            clipboard.setPrimaryClip(clip);
            if (listener != null) {
                listener.onRequestCopy();
            }
            getFragmentManager().beginTransaction().remove(this).commit();
        } else if (v.getId() == R.id.bt_wallet_request_share) {
            String shareBody = code;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share code");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
            if (listener != null) {
                listener.onRequestShare();
            }
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public interface OnRequestDoneListener {
        void onRequestShare();

        void onRequestCopy();
    }
}