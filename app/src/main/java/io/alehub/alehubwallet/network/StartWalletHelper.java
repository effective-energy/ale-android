package io.alehub.alehubwallet.network;

import android.os.AsyncTask;

import ru.dimzhur.alehublogictester.network.ServerRequestWorker;

/**
 * Created by dima on 2/21/18.
 */

public class StartWalletHelper extends AsyncTask<Void, Void, Boolean> {

    private OnStartWalletListener listener;

    public StartWalletHelper(OnStartWalletListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            return ServerRequestWorker.startWallet();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (listener != null) {
            listener.onWalletStart(aBoolean);
        }
    }

    public interface OnStartWalletListener {
        void onWalletStart(boolean done);
    }
}
