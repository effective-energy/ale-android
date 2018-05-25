package io.alehub.alehubwallet.network;

import android.os.AsyncTask;

import ru.dimzhur.alehublogictester.network.WalletRequestWorker;
import ru.dimzhur.alehublogictester.network.walletresponse.CreateWalletResponse;

/**
 * Created by dima on 2/21/18.
 */

public class WalletCreateHelper extends AsyncTask<Void, Void, String> {

    private OnWalletCreateListener createListener;

    public WalletCreateHelper(OnWalletCreateListener createListener) {
        this.createListener = createListener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        CreateWalletResponse cr = WalletRequestWorker.createWallet();
        if (cr != null && cr.getPublicKey() != null && cr.getPublicKey().length() > 0) {
            return cr.getPublicKey();
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (createListener != null) {
            createListener.onWalletCreate(s);
        }
    }

    public interface OnWalletCreateListener {
        void onWalletCreate(String publicKey);
    }
}
