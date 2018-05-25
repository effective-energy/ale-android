package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.db.DBTransactionsHelper;
import io.alehub.alehubwallet.listener.WalletListener;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.network.GetTransactionsHelper;
import io.alehub.alehubwallet.view.ButtonFont;
import io.alehub.alehubwallet.view.TextViewFont;

/**
 * Created by dima on 2/6/18.
 */

public class WalletSummaryFragment extends Fragment implements View.OnClickListener, WalletSendFragment.OnWalletSendListener, WalletRequestFragment.OnRequestDoneListener {

    private Toolbar tbRecentActivity;
    private LinearLayout llRecentActivity;
    private Button btSend, btRequest;


    private Wallet wallet;


    private WalletListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wallet_summary, null);

        tbRecentActivity = (Toolbar) v.findViewById(R.id.tb_wallet_fragment_recent_activity);
        btRequest = (ButtonFont) v.findViewById(R.id.bt_wallet_fragment_request);
        btSend = (ButtonFont) v.findViewById(R.id.bt_wallet_fragment_send);
        llRecentActivity = (LinearLayout) v.findViewById(R.id.ll_wallet_fragment_recent_activity);

        btSend.setOnClickListener(this);
        btRequest.setOnClickListener(this);


        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            wallet = (Wallet) savedInstanceState.getSerializable("wallet");
        } else if (getArguments() != null) {
            wallet = (Wallet) getArguments().getSerializable("wallet");
        }
        setWallet();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("wallet", wallet);
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
        setWallet();
    }

    private void setTransactions(List<Transaction> transactions) {
        int i = 0;
        llRecentActivity.removeAllViews();
        for (Transaction transaction : transactions) {
            Log.e("App", transaction.toString());
            try {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.wallet_transaction_element, null);
                TextView tvType = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_type);
                TextView tvDate = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_date);
                TextView tvTitle = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_title);
                TextView tvCount = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_count);

                if (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_SENT
                        || transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_SOLD) {
                    tvType.setTextColor(getResources().getColor(R.color.text_white));
                    tvCount.setTextColor(getResources().getColor(R.color.text_white));
                    tvCount.setBackgroundResource(R.drawable.bt_dark_normal);
                    tvType.setBackgroundResource(R.color.colorPrimary);
                    if (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_SENT) {
                        tvType.setText(R.string.sent);
                        tvTitle.setText(getString(R.string.send_to) + " " + transaction.getTo());
                    } else if (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_SOLD) {
                        tvType.setText(R.string.sold);
                    }
                } else if (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_BOUGHT
                        || transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_RECIVED) {
                    tvType.setTextColor(getResources().getColor(R.color.text_dark));
                    tvCount.setTextColor(getResources().getColor(R.color.text_dark));
                    tvCount.setBackgroundResource(R.drawable.bt_yellow_normal);
                    tvType.setBackgroundResource(R.color.bt_yellow_normal);
                    if (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_BOUGHT) {
                        tvType.setText(R.string.bought);
                    } else if (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_RECIVED) {
                        tvType.setText(R.string.recived);
                        tvTitle.setText(getString(R.string.recived_from) + " " + transaction.getFrom());
                    }
                }

                tvCount.setText(transaction.getCount() + "ALE");

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd @ HH:mm");
                tvDate.setText(sdf.format(new Date(transaction.getDate())));

                if (i % 2 == 0) {
                    v.setBackgroundResource(R.color.list_accent_bg);
                } else {
                    v.setBackgroundResource(R.color.list_normal_bg);
                }
                v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.recent_activity_height)));
                llRecentActivity.addView(v);
                i++;
                if (i > 4) {
                    break;
                }
            } catch (Exception e) {
                Log.e("App", "e", e);
            }
        }
    }

    private void setWallet() {
        llRecentActivity.removeAllViews();
        new GetTransactionsHelper(new GetTransactionsHelper.OnGetTransactionsListener() {
            @Override
            public void onGetTransactions(List<Transaction> transactions) {
                setTransactions(transactions);
            }
        }, getActivity()).execute(wallet.getPublicKey());

    }

    public void setListener(WalletListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_wallet_fragment_request) {
            showRequestDialog();
        } else if (v.getId() == R.id.bt_wallet_fragment_send) {
            showSendDialog();
        }
    }

    private void showSendDialog() {
        WalletSendFragment wsf = new WalletSendFragment();
        wsf.setCommis(0);
        wsf.setFrom(wallet.getPublicKey());
        wsf.setMax(wallet.getBalance());
        wsf.setListener(this);
        wsf.show(getChildFragmentManager(), "walletsend");
    }

    private void showRequestDialog() {
        Bundle args = new Bundle();
        args.putString("code", wallet.getPublicKey());
        WalletRequestFragment wsf = new WalletRequestFragment();
        wsf.setArguments(args);
        wsf.setListener(this);
        wsf.show(getChildFragmentManager(), "walletrequest");
    }

    @Override
    public void onRequestShare() {
        if (listener != null) {
            listener.showNotification(WalletListener.TYPE_COMPLEATE, R.string.qrcode_shared_success);
        }
    }

    @Override
    public void onRequestCopy() {
        if (listener != null) {
            listener.showNotification(WalletListener.TYPE_COMPLEATE, R.string.qrcode_copyd_success);
        }
    }

    @Override
    public void onWalletSend(long count, String address) {
        Transaction transaction = new Transaction();
        transaction.setCount(count);
        transaction.setTo(address);
        transaction.setFrom(wallet.getPublicKey());
        transaction.setDate(System.currentTimeMillis());
        DBTransactionsHelper.add(getActivity(), transaction);
        wallet.setBalance(wallet.getBalance() - count);
        if (listener != null) {
            listener.updateWallet(wallet);
        }
        setWallet();
    }
}
