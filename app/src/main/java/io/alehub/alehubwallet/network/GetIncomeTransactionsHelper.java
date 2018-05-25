package io.alehub.alehubwallet.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.db.DBTransactionsHelper;
import io.alehub.alehubwallet.db.DBWalletsHelper;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.model.Wallet;
import ru.dimzhur.alehublogictester.network.WalletRequestWorker;
import ru.dimzhur.alehublogictester.network.walletresponse.TransactionResponse;

/**
 * Created by dima on 3/1/18.
 */

public class GetIncomeTransactionsHelper extends AsyncTask<Void, Void, List<Transaction>> {

    private OnGetIncomeTransactionsListener listener;
    private Context context;

    public GetIncomeTransactionsHelper(OnGetIncomeTransactionsListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected List<Transaction> doInBackground(Void... voids) {
        try {

            List<Wallet> wallets = getWallets();
            List<Transaction> all = new ArrayList<>();
            for (Wallet wallet : wallets) {
                try {
                    List<TransactionResponse> responses = WalletRequestWorker.getWalletTransactions(wallet.getPublicKey());
                    List<Transaction> saved = DBTransactionsHelper.getAll(context, wallet.getPublicKey());
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
                    for (Transaction transaction : saved) {
                        if (transaction.getTo().toLowerCase().equals(wallet.getPublicKey().toLowerCase())) {
                            all.add(transaction);
                        }
                    }
                    Transaction.sortByTime(all);
                } catch (Exception e) {

                }
            }

            return all;

        } catch (Exception e) {
            Log.e("App", "e", e);
            return new ArrayList<>();
        }
    }

    protected List<Wallet> getWallets(Void... voids) {
        try {
            List<String> walletsKeys = WalletRequestWorker.getWallets();
            List<Wallet> saved = DBWalletsHelper.getAll(context);
            for (Wallet wallet : saved) {
                Log.e("App", "saved=" + wallet.toString());
            }
            for (String key : walletsKeys) {
                boolean add = true;
                for (Wallet wallet : saved) {
                    if (wallet.getPublicKey().equals(key)) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    Wallet wallet = new Wallet();
                    wallet.setName(key);
                    wallet.setPublicKey(key);
                    wallet.setBalance(0);
                    wallet = DBWalletsHelper.add(context, wallet);
                    if (wallet != null) {
                        saved.add(wallet);
                        Log.e("App", "add=" + wallet.toString());
                    }
                }
            }
            for (int i = 0; i < saved.size(); i++) {
                long balance = WalletRequestWorker.getWalletBalanceByTokenType(saved.get(i).getPublicKey(), "$@");
                saved.get(i).setBalance(balance);
                DBWalletsHelper.update(context, saved.get(i));
            }
            return saved;
        } catch (Exception e) {
            Log.e("App", "e", e);
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

    public interface OnGetIncomeTransactionsListener {
        void onGetTransactions(List<Transaction> transactions);
    }
}
