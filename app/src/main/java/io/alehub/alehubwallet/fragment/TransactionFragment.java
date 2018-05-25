package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.view.TextViewFont;
import io.alehub.alehubwallet.view.ToolbarFont;

/**
 * Created by dima on 24.03.18.
 */

public class TransactionFragment extends Fragment {

    private TextViewFont tvType, tvCount, tvTitle, tvDate;
    private ToolbarFont toolbar;


    private Wallet wallet;
    private Transaction transaction;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.transaction_fragment, null);

        toolbar = (ToolbarFont) v.findViewById(R.id.toolbar);
        tvCount = (TextViewFont) v.findViewById(R.id.tv_transaction_element_count);
        tvDate = (TextViewFont) v.findViewById(R.id.tv_transaction_element_date);
        tvTitle = (TextViewFont) v.findViewById(R.id.tv_transaction_element_title);
        tvType = (TextViewFont) v.findViewById(R.id.tv_transaction_element_type);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (savedInstanceState != null) {
            transaction = (Transaction) savedInstanceState.getSerializable("transaction");
            wallet = (Wallet) savedInstanceState.getSerializable("wallet");
        } else if (getArguments() != null) {
            transaction = (Transaction) getArguments().getSerializable("transaction");
            wallet = (Wallet) getArguments().getSerializable("wallet");
        }

        setTransactions();

        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("transaction", transaction);
        outState.putSerializable("wallet", wallet);
    }


    private int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    private String getDateTitle(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) && now.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            return getString(R.string.today);
        } else if (now.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) - 1 && now.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            return getString(R.string.yesterday);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
            return sdf.format(date);
        }

    }

    private void setTransactions() {
        try {
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
        } catch (Exception e) {

        }

    }
}
