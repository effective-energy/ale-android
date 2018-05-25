package io.alehub.alehubwallet;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.alehub.alehubwallet.fragment.TransactionFragment;
import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.model.Wallet;

/**
 * Created by dima on 24.03.18.
 */

public class TransactionActivity extends LangBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        Wallet wallet = (Wallet) getIntent().getSerializableExtra("wallet");
        Transaction transaction = (Transaction) getIntent().getSerializableExtra("transaction");
        Bundle b = new Bundle();
        b.putSerializable("wallet", wallet);
        b.putSerializable("transaction", transaction);
        TransactionFragment tf = new TransactionFragment();
        tf.setArguments(b);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, tf)
                .commit();
    }
}
