package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.network.GetIncomeTransactionsHelper;
import io.alehub.alehubwallet.view.TextViewFont;
import io.alehub.alehubwallet.view.ToolbarFont;

/**
 * Created by dima on 24.03.18.
 */

public class NotificationsFragment extends Fragment {

    private ToolbarFont toolbar;
    private LinearLayout llContent;
    private LinearLayout llNo;
    private ProgressBar pbLoad;

    private List<Transaction> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notificattions_fragment, null);

        llNo = (LinearLayout) v.findViewById(R.id.ll_notifications_no);
        toolbar = (ToolbarFont) v.findViewById(R.id.toolbar);
        llContent = (LinearLayout) v.findViewById(R.id.ll_notifications_content);
        pbLoad = (ProgressBar) v.findViewById(R.id.pb_notifications_load);


        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadContent();
    }

    private void loadContent() {
        llNo.setVisibility(View.INVISIBLE);
        pbLoad.setVisibility(View.VISIBLE);
        new GetIncomeTransactionsHelper(new GetIncomeTransactionsHelper.OnGetIncomeTransactionsListener() {
            @Override
            public void onGetTransactions(List<Transaction> transactions) {
                items = transactions;
                setTransactions();
                if (transactions.size() == 0) {
                    llNo.setVisibility(View.VISIBLE);
                }
            }
        }, getActivity()).execute();

    }

    private void setTransactions() {


        int i = 0;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        try {
            for (Transaction transaction : items) {
                if (i == 0 || day != getDay(new Date(transaction.getDate()))) {
                    day = getDay(new Date(transaction.getDate()));
                    View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.wallet_recent_activity_title_element, null);
                    TextView tv = (TextViewFont) v1.findViewById(R.id.tv_wallet_recent_activity_title);
                    tv.setText(getDateTitle(new Date(transaction.getDate())));
                    v1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    llContent.addView(v1);
                }


                View v = LayoutInflater.from(getActivity()).inflate(R.layout.wallet_transaction_element, null);
                TextView tvType = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_type);
                TextView tvDate = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_date);
                TextView tvTitle = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_title);
                TextView tvCount = (TextViewFont) v.findViewById(R.id.tv_wallet_transaction_element_count);


                tvType.setTextColor(getResources().getColor(R.color.text_dark));
                tvCount.setTextColor(getResources().getColor(R.color.text_dark));
                tvCount.setBackgroundResource(R.drawable.bt_yellow_normal);
                tvType.setBackgroundResource(R.color.bt_yellow_normal);
                tvType.setText(R.string.recived);
                tvTitle.setText(getString(R.string.recived_from) + " " + transaction.getFrom());


                tvCount.setText(transaction.getCount() + "ALE");

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd @ HH:mm");
                tvDate.setText(sdf.format(new Date(transaction.getDate())));

                if (i % 2 == 0) {
                    v.setBackgroundResource(R.color.list_accent_bg);
                } else {
                    v.setBackgroundResource(R.color.list_normal_bg);
                }
                v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.recent_activity_height)));
                llContent.addView(v);
                i++;
            }
            pbLoad.setVisibility(View.INVISIBLE);
            llContent.setVisibility(View.VISIBLE);
        } catch (Exception e) {

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
