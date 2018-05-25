package io.alehub.alehubwallet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.alehub.alehubwallet.QRCodeScanActivity;
import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.adapter.WalletsAdapter;
import io.alehub.alehubwallet.dialog.WaitDialog;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.network.GetWalletsHelper;
import io.alehub.alehubwallet.network.SendMoneyHelper;
import io.alehub.alehubwallet.view.EditTextFont;
import io.alehub.alehubwallet.view.NotificationLayout;

/**
 * Created by dima on 2/6/18.
 */

public class SendFragment extends Fragment implements View.OnClickListener {

    private EditTextFont edAmount, edAddress, edTotal;
    private TextInputLayout tilAmount;
    private NotificationLayout nlNotify;
    private Toolbar toolbar;
    private Spinner spWallets;

    private Wallet curWallet;

    private List<Wallet> wallets = new ArrayList<>();


    private WaitDialog waitDialog;

    private String strAmount = "";
    private int commis = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.send_fragment, null);


        waitDialog = new WaitDialog(getActivity());

        edAmount = (EditTextFont) v.findViewById(R.id.ed_send_amount);
        edAddress = (EditTextFont) v.findViewById(R.id.ed_send_address);
        edTotal = (EditTextFont) v.findViewById(R.id.ed_send_total);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        nlNotify = (NotificationLayout) v.findViewById(R.id.nl_send_notify);
        spWallets = (Spinner) v.findViewById(R.id.sp_send_wallet);

        tilAmount = (TextInputLayout) v.findViewById(R.id.til_send_amount);


        // tilAmount.setHint(getString(R.string.address_caps) + " (" + getString(R.string.max) + ": " + max + " ALE)");

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
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String amount = edAmount.getText().toString();
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

        toolbar.inflateMenu(R.menu.send);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_send_qrcode) {
                    readQrCode();
                } else if (item.getItemId() == R.id.menu_send_send) {
                    send();
                }
                return false;
            }
        });

        spWallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curWallet = wallets.get(position);
                tilAmount.setHint(getString(R.string.amount_caps) + " " + getString(R.string.max) + " " + curWallet.getBalance() + " ALE");
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
        loadWallets();
    }

    private void loadWallets() {
        waitDialog.show(R.string.please_wait);
        new GetWalletsHelper(getActivity(), new GetWalletsHelper.OnGetWalletsListener() {
            @Override
            public void onGetWallets(List<Wallet> wls) {
                waitDialog.hide();
                wallets.clear();
                wallets.addAll(wls);
                spWallets.setAdapter(new WalletsAdapter(getActivity(), wallets));
                if (wallets.size() > 0) {
                    curWallet = wallets.get(0);
                    tilAmount.setHint(getString(R.string.amount_caps) + " " + getString(R.string.max) + " " + curWallet.getBalance() + " ALE");

                }
            }
        }).execute();
    }


    private void readQrCode() {
        startActivityForResult(new Intent(getActivity(), QRCodeScanActivity.class), 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            String code = data.getStringExtra("data");
            edAddress.setText(code);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void send() {
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

        if (count + commis > curWallet.getBalance()) {
            nlNotify.showError(R.string.send_limit);
        } else if (count == 0) {
            nlNotify.showError(R.string.send_zero);
        } else if (address.length() < 1) {
            nlNotify.showError(R.string.send_no_address);
        } else {
            waitDialog.show(R.string.please_wait);

            Transaction transaction = new Transaction();
            transaction.setFrom(curWallet.getPublicKey());
            transaction.setTo(address);
            transaction.setCount(count);

            new SendMoneyHelper(new SendMoneyHelper.OnMoneySendListener() {
                @Override
                public void onMoneySend(Transaction transaction) {
                    waitDialog.hide();
                    if (transaction != null) {
                        nlNotify.showCompleate(R.string.send_done);
                    } else {
                        nlNotify.showError(R.string.send_error);
                    }
                }
            }).execute(transaction);

        }
    }


    @Override
    public void onClick(View v) {
    }


}