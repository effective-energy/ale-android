package io.alehub.alehubwallet.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.regex.Pattern;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.dialog.WaitDialog;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.network.SendMoneyHelper;
import io.alehub.alehubwallet.view.ButtonFont;
import io.alehub.alehubwallet.view.EditTextFont;
import io.alehub.alehubwallet.view.NotificationLayout;

/**
 * Created by dima on 2/6/18.
 */

public class WalletSendFragment extends DialogFragment implements View.OnClickListener {

    private EditText edAmount, edAddress, edTotal;
    private Button btSend;
    private ImageButton ibClose;
    private TextInputLayout tilAmount;
    private NotificationLayout nlNotify;

    private OnWalletSendListener listener;
    private long max = 0;
    private long commis = 0;

    private String from;
    private String strAmount;

    private WaitDialog waitDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wallet_send, null);

        btSend = (ButtonFont) v.findViewById(R.id.bt_wallet_send_send);
        ibClose = (ImageButton) v.findViewById(R.id.bt_wallet_send_close);

        waitDialog = new WaitDialog(getActivity());

        edAmount = (EditTextFont) v.findViewById(R.id.ed_wallet_send_amount);
        edAddress = (EditTextFont) v.findViewById(R.id.ed_wallet_send_address);
        edTotal = (EditTextFont) v.findViewById(R.id.ed_wallet_send_total);

        nlNotify = (NotificationLayout) v.findViewById(R.id.nl_wallet_send_notify);

        tilAmount = (TextInputLayout) v.findViewById(R.id.til_wallet_send_amount);

        btSend.setOnClickListener(this);
        ibClose.setOnClickListener(this);

        tilAmount.setHint(getString(R.string.address_caps) + " (" + getString(R.string.max) + ": " + max + " ALE)");

        edAmount.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = edAmount.getText().toString().length();
                int cursor = edAmount.getSelectionStart();
                int minSelection = length - 3;
                if (cursor < minSelection) {
                } else {
                    try {
                        int currentCursor = edAmount.getSelectionStart();
                        edAmount.setText(strAmount);
                        edAmount.setSelection(currentCursor - 1);
                    }catch (Exception e){}
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String amount = edAmount.getText().toString();
                amount = amount.replaceAll(Pattern.quote(","), "");
                amount = amount.replaceAll(Pattern.quote(" "), "");
                amount = amount.replaceAll(Pattern.quote("ALE"), "");
                double count = 0;
                try {
                    count = Double.parseDouble(amount);

                } catch (Exception e) {
                }
                String total = (count + commis) + " ALE" + "(" + getString(R.string.with) + " " + commis + " " + getString(R.string.of_fees) + ")";
                edTotal.setText(total);
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strAmount = edAmount.getText().toString();
            }
        });

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return v;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public void setCommis(long commis) {
        this.commis = commis;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setListener(OnWalletSendListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = getResources().getDimensionPixelOffset(R.dimen.change_password_height);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_wallet_send_close) {
            getFragmentManager().beginTransaction().remove(this).commit();
        } else if (v.getId() == R.id.bt_wallet_send_send) {
            if (listener != null) {
                String address = edAddress.getText().toString();
                String amount = edAmount.getText().toString();
                amount = amount.replaceAll(Pattern.quote(","), "");
                amount = amount.replaceAll(Pattern.quote(" "), "");
                amount = amount.replaceAll(Pattern.quote("ALE"), "");
                long count = 0;
                try {
                    count = (long) Double.parseDouble(amount);
                } catch (Exception e) {
                    Log.e("App", "r", e);
                }

                if (count + commis > max) {
                    nlNotify.showError(R.string.send_limit);
                } else if (count == 0) {
                    nlNotify.showError(R.string.send_zero);
                } else if (address.length() < 1) {
                    nlNotify.showError(R.string.send_no_address);
                } else {
                    waitDialog.show(R.string.please_wait);

                    Transaction transaction = new Transaction();
                    transaction.setFrom(from);
                    transaction.setTo(address);
                    transaction.setCount(count);

                    new SendMoneyHelper(new SendMoneyHelper.OnMoneySendListener() {
                        @Override
                        public void onMoneySend(Transaction transaction) {
                            waitDialog.hide();
                            if(transaction!= null){
                                listener.onWalletSend(transaction.getCount(), transaction.getTo());
                                getFragmentManager().beginTransaction().remove(WalletSendFragment.this).commit();
                            }else{
                                nlNotify.showError(R.string.send_error);
                            }
                        }
                    }).execute(transaction);

                }

            }
        }
    }

    public interface OnWalletSendListener {
        void onWalletSend(long count, String address);
    }
}