package io.alehub.alehubwallet.network;

import android.os.AsyncTask;
import android.util.Log;

import ru.dimzhur.alehublogictester.network.WalletRequestWorker;
import ru.dimzhur.alehublogictester.network.walletresponse.CreateWalletResponse;

/**
 * Created by dima on 2/21/18.
 */

public class WalletImportHelper extends AsyncTask<String, Void, String> {

    private OnWalletImportListener importListener;

    public WalletImportHelper(OnWalletImportListener importListener) {
        this.importListener = importListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String secretKey;
        try {
            if (strings != null && strings.length > 0 && strings[0] != null) {
                secretKey = strings[0];
                CreateWalletResponse cr = WalletRequestWorker.importWallet(secretKey);
                if (cr != null && cr.getPublicKey() != null && cr.getPublicKey().length() > 0) {
                    return cr.getPublicKey();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("App", "e", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (importListener != null) {
            importListener.onWalletImport(s);
        }
    }

    public interface OnWalletImportListener {
        void onWalletImport(String publickKey);
    }
}
