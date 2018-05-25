package io.alehub.alehubwallet.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.db.DBWalletsHelper;
import io.alehub.alehubwallet.model.Wallet;
import ru.dimzhur.alehublogictester.network.WalletRequestWorker;

/**
 * Created by dima on 2/22/18.
 */

public class GetWalletsHelper extends AsyncTask<Void, Void, List<Wallet>> {

    private Context context;
    private OnGetWalletsListener listener;

    public GetWalletsHelper(Context context, OnGetWalletsListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<Wallet> doInBackground(Void... voids) {
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
    protected void onPostExecute(List<Wallet> wallets) {
        super.onPostExecute(wallets);
        if (listener != null) {
            listener.onGetWallets(wallets);
        }
    }

    public interface OnGetWalletsListener {
        void onGetWallets(List<Wallet> wallets);
    }
}
