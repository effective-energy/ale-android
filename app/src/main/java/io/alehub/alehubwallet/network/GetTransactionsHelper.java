package io.alehub.alehubwallet.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.alehub.alehubwallet.db.DBTransactionsHelper;
import io.alehub.alehubwallet.model.Transaction;
import ru.dimzhur.alehublogictester.network.WalletRequestWorker;
import ru.dimzhur.alehublogictester.network.walletresponse.TransactionResponse;

/**
 * Created by dima on 3/1/18.
 */

public class GetTransactionsHelper extends AsyncTask<String, Void, List<Transaction>> {

    private OnGetTransactionsListener listener;
    private Context context;

    public GetTransactionsHelper(OnGetTransactionsListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected List<Transaction> doInBackground(String... strings) {
        try {
            if (strings != null && strings.length > 0 && strings[0] != null) {
                List<TransactionResponse> responses = WalletRequestWorker.getWalletTransactions(strings[0]);
                List<Transaction> saved = DBTransactionsHelper.getAll(context, strings[0]);
                List<Transaction> transactions = Transaction.fromResponses(responses);
                for (Transaction transaction : transactions) {
                    boolean add = true;
                    for (Transaction s : saved) {
                        if (s.getDate() == transaction.getDate() && s.getCount() == transaction.getCount()
                                && s.getFrom().equals(transaction.getFrom())
                                && s.getTo().equals(transaction.getTo())) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        saved.add(transaction);
                        DBTransactionsHelper.add(context, transaction);
                    }
                }
                Transaction.sortByTime(saved);
                return saved;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("App", "e",e);
            return new ArrayList<>();
        }
    }

    @Override
    protected void onPostExecute(List<Transaction> transactions) {
        super.onPostExecute(transactions);
        if (listener != null) {
            listener.onGetTransactions(transactions);
        }
    }

    public interface OnGetTransactionsListener {
        void onGetTransactions(List<Transaction> transactions);
    }
}
