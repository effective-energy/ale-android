package io.alehub.alehubwallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.TransactionActivity;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.network.GetTransactionsHelper;
import io.alehub.alehubwallet.view.EditTextFont;
import io.alehub.alehubwallet.view.TextViewFont;

/**
 * Created by dima on 2/6/18.
 */

public class WalletTransactionsFragment extends Fragment {

    private LinearLayout llList, llLoad;
    private EditText edSearch;
    private Wallet wallet;
    private Spinner spType;
    private LinearLayout llNo;


    private static int TYPE_ALL = 0;
    private static int TYPE_INCOME = 1;
    private static int TYPE_EXPEND = 2;

    private String textFilter;
    private int typeFilter;

    private List<Transaction> items = new ArrayList<>();
    private List<Transaction> all = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wallet_transactions_fragment, null);

        llNo = (LinearLayout) v.findViewById(R.id.ll_transactions_no);
        llList = (LinearLayout) v.findViewById(R.id.ll_wallet_transactions_fragment_list);
        llLoad = (LinearLayout) v.findViewById(R.id.ll_wallet_transactions_fragment_load);
        edSearch = (EditTextFont) v.findViewById(R.id.ed_wallet_transactions_fragment_search);
        spType = (Spinner) v.findViewById(R.id.sp_wallet_transactions_fragment_type);

        llNo.setVisibility(View.INVISIBLE);


        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textFilter = edSearch.getText().toString();
                filter();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeFilter = position;
                filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        new GetTransactionsHelper(new GetTransactionsHelper.OnGetTransactionsListener() {
            @Override
            public void onGetTransactions(List<Transaction> transactions) {
                all = transactions;
                filter();
            }
        }, getActivity()).execute(wallet.getPublicKey());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("wallet", wallet);
    }

    private void filter() {
        llNo.setVisibility(View.INVISIBLE);
        items.clear();
        for (Transaction tr : all) {
            if (checkByText(tr) && checkByType(tr)) {
                items.add(tr);
            }
        }
        setTransactions();
    }

    private boolean checkByText(Transaction wra) {
        if (textFilter == null || textFilter.length() < 1) {
            return true;
        } else {
            return wra.getFrom().toLowerCase().indexOf(textFilter.toLowerCase()) > -1
                    || wra.getTo().toLowerCase().indexOf(textFilter.toLowerCase()) > -1;
        }
    }

    private boolean checkByType(Transaction transaction) {
        if (typeFilter == TYPE_ALL) {
            return true;
        } else if (typeFilter == TYPE_EXPEND) {
            return (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_SENT || transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_SOLD);
        } else if (typeFilter == TYPE_INCOME) {
            return (transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_RECIVED || transaction.getType(wallet.getPublicKey()) == Transaction.TYPE_BOUGHT);
        } else {
            return false;
        }
    }

    private void setTransactions() {
        if (wallet != null) {
            llList.removeAllViews();
            Transaction.sortByTime(items);
        }

        int i = 0;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        try {
            for (final Transaction transaction : items) {
                if (i == 0 || day != getDay(new Date(transaction.getDate()))) {
                    day = getDay(new Date(transaction.getDate()));
                    View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.wallet_recent_activity_title_element, null);
                    TextView tv = (TextViewFont) v1.findViewById(R.id.tv_wallet_recent_activity_title);
                    tv.setText(getDateTitle(new Date(transaction.getDate())));
                    v1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    llList.addView(v1);
                }


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
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), TransactionActivity.class);
                        i.putExtra("wallet", wallet);
                        i.putExtra("transaction", transaction);
                        startActivity(i);
                    }
                });
                llList.addView(v);
                i++;
            }
            llLoad.setVisibility(View.INVISIBLE);
            llList.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
        if (items.size() < 1) {
            llNo.setVisibility(View.VISIBLE);
        } else {
            llNo.setVisibility(View.INVISIBLE);
        }
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
}
