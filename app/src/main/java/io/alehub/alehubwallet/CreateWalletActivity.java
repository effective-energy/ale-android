package io.alehub.alehubwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import io.alehub.alehubwallet.db.DBWalletsHelper;
import io.alehub.alehubwallet.dialog.WaitDialog;
import io.alehub.alehubwallet.fragment.createwallet.AddWalletTypeFragment;
import io.alehub.alehubwallet.fragment.createwallet.CreateWalletFragment;
import io.alehub.alehubwallet.fragment.createwallet.ImportWalletFragment;
import io.alehub.alehubwallet.fragment.createwallet.RecoveryPhraseAboutFragment;
import io.alehub.alehubwallet.fragment.createwallet.RecoveryPhraseAskFragment;
import io.alehub.alehubwallet.fragment.createwallet.RecoveryPhraseShowFragment;
import io.alehub.alehubwallet.fragment.createwallet.RedemptionWalletFragment;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.network.WalletCreateHelper;
import io.alehub.alehubwallet.network.WalletImportHelper;
import io.alehub.alehubwallet.view.NotificationLayout;

/**
 * Created by dima on 2/21/18.
 */

public class CreateWalletActivity extends LangBaseActivity {

    private final static String SELECT_TYPE_FRAGMENT = "select_type";
    private final static String IMPORT_FRAGMENT = "import";
    private final static String NEW_FRAGMENT = "new";
    private final static String REDEMPTION_FRAGMENT = "redemption";
    private final static String RECOVERY_ABOUT_FRAGMENT = "recovery_about";
    private final static String RECOVERY_ASK_FRAGMENT = "recovery_ask";
    private final static String RECOVERY_SHOW_FRAGMENT = "recovery_show";

    private AddWalletTypeFragment selectTypeFragment;
    private ImportWalletFragment importWalletFragment;
    private CreateWalletFragment createWalletFragment;
    private RedemptionWalletFragment redemptionWalletFragment;

    private NotificationLayout nlNotify;

    private int type = -1;

    private String name = "";
    private String key = "";
    private String cert = "";
    private String recovery = "";

    private WaitDialog waitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waitDialog = new WaitDialog(this);
        setContentView(R.layout.content_notify);
        nlNotify = (NotificationLayout)findViewById(R.id.notify);
        setSelectTypeFragment();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            try {
                System.gc();
            } catch (Exception e) {
            }
            FragmentManager fsm = getSupportFragmentManager();
            FragmentTransaction ftransaction = fsm.beginTransaction();
            fsm.popBackStack();
            ftransaction.commit();

        } else {
            finish();
        }
    }

    public void create() {
        waitDialog.show(R.string.please_wait);
        if (cert.length() > 0) {

        } else if (key.length() > 0) {
            importWallet();
        }else{
            createWallet();
        }
    }

    private void setResult(Wallet wallet) {
        Intent intent = new Intent();
        intent.putExtra("wallet", wallet);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void createWallet() {
        final Wallet wallet = new Wallet(name, 0);
        new WalletCreateHelper(new WalletCreateHelper.OnWalletCreateListener() {
            @Override
            public void onWalletCreate(String publicKey) {
                waitDialog.hide();
                if (publicKey != null) {
                    wallet.setPublicKey(publicKey);
                    Wallet w  =  DBWalletsHelper.add(CreateWalletActivity.this, wallet);
                    setResult(w);
                }else{
                    nlNotify.showError(R.string.can_not_create_wallet);
                }
            }
        }).execute();
    }

    private void importWallet() {
        final Wallet wallet = new Wallet( name,0);
        new WalletImportHelper(new WalletImportHelper.OnWalletImportListener() {
            @Override
            public void onWalletImport(String publickKey) {
                waitDialog.hide();
                if (publickKey != null) {
                    wallet.setPublicKey(publickKey);
                    Wallet w  =  DBWalletsHelper.add(CreateWalletActivity.this, wallet);
                    setResult(w);
                }else{
                    nlNotify.showError(R.string.can_not_import_wallet);
                }
            }
        }).execute(key);
    }

    public void setSelectTypeFragment() {
        if (selectTypeFragment == null) {
            selectTypeFragment = new AddWalletTypeFragment();
        }
        selectTypeFragment.setTypeSelectListener(new AddWalletTypeFragment.OnTypeSelectListener() {
            @Override
            public void onTypeSelect(int tp) {
                if (tp == AddWalletTypeFragment.TYPE_NEW) {
                    setCreateWalletFragment();
                    type = AddWalletTypeFragment.TYPE_NEW;
                } else if (tp == AddWalletTypeFragment.TYPE_IMPORT) {
                    setImportWalletFragment();
                    type = AddWalletTypeFragment.TYPE_IMPORT;
                }
                if (tp == AddWalletTypeFragment.TYPE_REDEMPTION) {
                    setRedemptionWalletFragment();
                    type = AddWalletTypeFragment.TYPE_REDEMPTION;
                }
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, selectTypeFragment)
                .addToBackStack(SELECT_TYPE_FRAGMENT)
                .commit();
    }

    public void setImportWalletFragment() {
        if (importWalletFragment == null) {
            importWalletFragment = new ImportWalletFragment();
        }
        importWalletFragment.setDoneListener(new ImportWalletFragment.OnImportWalletNameDoneListener() {
            @Override
            public void onImportNameDone(String name, String secretKey) {
                CreateWalletActivity.this.name = name;
                CreateWalletActivity.this.key = secretKey;
                setRecoveryAboutFragment();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, importWalletFragment)
                .addToBackStack(IMPORT_FRAGMENT)
                .commit();
    }

    public void setRedemptionWalletFragment() {
        if (redemptionWalletFragment == null) {
            redemptionWalletFragment = new RedemptionWalletFragment();
        }
        redemptionWalletFragment.setDoneListener(new RedemptionWalletFragment.OnRedemptionWalletNameDoneListener() {
            @Override
            public void onRedemptionNameDone(String name, String key, String cert) {
                CreateWalletActivity.this.name = name;
                CreateWalletActivity.this.key = key;
                CreateWalletActivity.this.cert = cert;
                setRecoveryAboutFragment();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, redemptionWalletFragment)
                .addToBackStack(REDEMPTION_FRAGMENT)
                .commit();
    }

    public void setCreateWalletFragment() {
        if (createWalletFragment == null) {
            createWalletFragment = new CreateWalletFragment();
        }
        createWalletFragment.setDoneListener(new CreateWalletFragment.OnCreateWalletNameDoneListener() {
            @Override
            public void onCreateNameDone(String name) {
                CreateWalletActivity.this.name = name;
                setRecoveryAboutFragment();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, createWalletFragment)
                .addToBackStack(NEW_FRAGMENT)
                .commit();
    }

    public void setRecoveryAboutFragment() {
        RecoveryPhraseAboutFragment aboutFragment = new RecoveryPhraseAboutFragment();
        aboutFragment.setConfirmListener(new RecoveryPhraseAboutFragment.OnAboutConfirmListener() {
            @Override
            public void onAboutConfirm() {
                setRecoveryAskFragment();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, aboutFragment)
                .addToBackStack(RECOVERY_ABOUT_FRAGMENT)
                .commit();
    }


    public void setRecoveryAskFragment() {
        RecoveryPhraseAskFragment askFragment = new RecoveryPhraseAskFragment();
        askFragment.setDoneListener(new RecoveryPhraseAskFragment.OnRecoveryPhraseAskDoneListener() {
            @Override
            public void onRecoveryPhraseAskDone(String recoveryPhrase) {
                recovery = recoveryPhrase;
                setRecoveryShowFragment();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, askFragment)
                .addToBackStack(RECOVERY_ASK_FRAGMENT)
                .commit();
    }

    public void setRecoveryShowFragment() {
        RecoveryPhraseShowFragment showFragment = new RecoveryPhraseShowFragment();
        showFragment.setDoneListener(new RecoveryPhraseShowFragment.OnShowPhraseDoneListener() {
            @Override
            public void onShowPhraseDone() {
                create();
            }
        });
        Bundle b = new Bundle();
        b.putString("phrase", recovery);
        showFragment.setArguments(b);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, showFragment)
                .addToBackStack(RECOVERY_SHOW_FRAGMENT)
                .commit();
    }
}
