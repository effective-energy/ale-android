package io.alehub.alehubwallet.network;

import android.os.AsyncTask;

import io.alehub.alehubwallet.model.Transaction;
import ru.dimzhur.alehublogictester.network.WalletRequestWorker;
import ru.dimzhur.alehublogictester.network.walletresponse.TransactionResponse;

/**
 * Created by dima on 2/27/18.
 */

public class SendMoneyHelper extends AsyncTask<Transaction, Void, Transaction> {

    private OnMoneySendListener listener;

    public SendMoneyHelper(OnMoneySendListener listener) {
        this.listener = listener;
    }

    @Override
    protected Transaction doInBackground(Transaction... transactions) {
        if (transactions != null && transactions.length > 0 && transactions[0] != null) {
            Transaction send = transactions[0];
            TransactionResponse resp = WalletRequestWorker.createTransaction(send.getFrom(), send.getTo(), (int) send.getCount());
            if (resp != null) {
                return Transaction.fromResponse(resp);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Transaction transaction) {
        super.onPostExecute(transaction);
        if (listener != null) {
            listener.onMoneySend(transaction);
        }
    }

    public interface OnMoneySendListener {
        void onMoneySend(Transaction transaction);
    }

}
