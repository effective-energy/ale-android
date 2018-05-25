package io.alehub.alehubwallet.listener;

import io.alehub.alehubwallet.model.Wallet;

/**
 * Created by dima on 2/6/18.
 */

public interface WalletListener {

    final int TYPE_ERROR = 1;
    final int TYPE_COMPLEATE = 2;

    void showNotification(int type, int text);

    void updateWallet(Wallet wallet);

    void onDeleteWallet(Wallet wallet);
}
