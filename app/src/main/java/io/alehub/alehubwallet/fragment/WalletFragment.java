package io.alehub.alehubwallet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.CreateWalletActivity;
import io.alehub.alehubwallet.MainActivity;
import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.adapter.WalletSpinnerTitleAdapter;
import io.alehub.alehubwallet.db.DBWalletsHelper;
import io.alehub.alehubwallet.dialog.WaitDialog;
import io.alehub.alehubwallet.listener.WalletListener;
import io.alehub.alehubwallet.model.Wallet;
import io.alehub.alehubwallet.network.GetWalletsHelper;
import io.alehub.alehubwallet.view.NotificationLayout;
import io.alehub.alehubwallet.view.TextViewFont;
import io.alehub.alehubwallet.view.ToolbarFont;

/**
 * Created by dima on 1/30/18.
 */

public class WalletFragment extends Fragment implements View.OnClickListener, WalletListener {

    private ToolbarFont toolbar;
    private LinearLayout llAddWallet, llWallets, llWalletSettings, llSettings, llTransactions;
    private TextViewFont tvWalletName, tvWalletCash;
    private NotificationLayout nlNotify;
    private Spinner spTitle;

    private int curFragment = 0;

    private static final int FRAGMENT_APP_SETTINGS = 0;
    private static final int FRAGMENT_TRANSACTIONS = 1;
    private static final int FRAGMENT_SETTINGS = 2;

    private DrawerLayout dlDrawer;
    private NavigationView nvDrawer;

    private WalletSpinnerTitleAdapter titleAdapter;

    private int curWallet = -1;

    private List<Wallet> wallets = new ArrayList<>();


    private WaitDialog waitDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wallet_fragment, null);

        toolbar = (ToolbarFont) v.findViewById(R.id.toolbar);

        tvWalletName = (TextViewFont) v.findViewById(R.id.tv_wallet_fragment_title);
        tvWalletCash = (TextViewFont) v.findViewById(R.id.tv_wallet_fragment_title_count);
        nlNotify = (NotificationLayout) v.findViewById(R.id.nl_wallet_notify);

        dlDrawer = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) v.findViewById(R.id.nvView);

        spTitle = (Spinner) v.findViewById(R.id.sp_wallet_fragment_select_wallet);

        waitDialog = new WaitDialog(getActivity());

        llAddWallet = (LinearLayout) nvDrawer.getHeaderView(0).findViewById(R.id.ll_wallet_drawer_header_add_wallet);
        llSettings = (LinearLayout) nvDrawer.getHeaderView(0).findViewById(R.id.ll_wallet_drawer_header_app_settings);
        llWalletSettings = (LinearLayout) nvDrawer.getHeaderView(0).findViewById(R.id.ll_wallet_drawer_header_walllet_settings);
        llWallets = (LinearLayout) nvDrawer.getHeaderView(0).findViewById(R.id.ll_wallet_drawer_header_wallets);
        llTransactions = (LinearLayout) nvDrawer.getHeaderView(0).findViewById(R.id.ll_wallet_drawer_header_walllet_transactions);

        ActionBarDrawerToggle drawerToggle = setupDrawerToggle();
        dlDrawer.addDrawerListener(drawerToggle);

        llAddWallet.setOnClickListener(this);
        llSettings.setOnClickListener(this);
        llWalletSettings.setOnClickListener(this);
        llTransactions.setOnClickListener(this);


        titleAdapter = new WalletSpinnerTitleAdapter(getActivity());
        spTitle.setAdapter(titleAdapter);

        spTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curWallet = position;
                if (wallets.size() > 0) {
                    setWallet();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getWallets();

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        return v;
    }


    private void setWallet() {
        try {
            Wallet wallet = wallets.get(curWallet);
            tvWalletCash.setText(wallet.getBalance() + " " + getString(R.string.ale));
            tvWalletName.setText(wallet.getName());
            setTransactions();
        } catch (Exception e) {
        }

    }


    private void setSettings() {
        curFragment = FRAGMENT_SETTINGS;
        Wallet wallet = wallets.get(curWallet);
        tvWalletCash.setText(wallet.getBalance() + " " + getString(R.string.ale));
        tvWalletName.setText(wallet.getName());

        WalletSettingFragment wsf = new WalletSettingFragment();
        wsf.setListener(this);
        Bundle args = new Bundle();
        args.putSerializable("wallet", wallets.get(curWallet));
        wsf.setArguments(args);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.wallet_content, wsf).commit();

    }

    private void setTransactions() {
        curFragment = FRAGMENT_TRANSACTIONS;
        Wallet wallet = wallets.get(curWallet);
        tvWalletCash.setText(wallet.getBalance() + " " + getString(R.string.ale));
        tvWalletName.setText(wallet.getName());

        WalletTransactionsFragment wsf = new WalletTransactionsFragment();
        //  wsf.setListener(this);
        Bundle args = new Bundle();

        args.putSerializable("wallet", wallets.get(curWallet));
        wsf.setArguments(args);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.wallet_content, wsf).commit();
    }


    private void getWallets() {
        new GetWalletsHelper(getActivity(), new GetWalletsHelper.OnGetWalletsListener() {
            @Override
            public void onGetWallets(List<Wallet> wls) {
                wallets.clear();
                wallets.addAll(wls);
                waitDialog.hide();
                if (wallets.size() == 0) {
                    createWallet();
                } else {
                    curWallet = 0;
                    titleAdapter.clear();
                    for (Wallet w : wallets) {
                        titleAdapter.addElement(w);
                    }
                    setWallet();
                }
            }
        }).execute();
    }

    private void getWalletsWithSummary() {
        new GetWalletsHelper(getActivity(), new GetWalletsHelper.OnGetWalletsListener() {
            @Override
            public void onGetWallets(List<Wallet> wls) {
                wallets.clear();
                wallets.addAll(wls);
                waitDialog.hide();

                if (wallets.size() == 0) {
                    createWallet();
                } else {
                    curWallet = wallets.size() - 1;
                    setTransactions();
                }
            }
        }).execute();
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(getActivity(), dlDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void createWallet() {
        startActivityForResult(new Intent(getActivity(), CreateWalletActivity.class), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            getWalletsWithSummary();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_wallet_drawer_header_add_wallet) {
            createWallet();
            dlDrawer.closeDrawers();
        } else if (v.getId() == R.id.ll_wallet_drawer_header_app_settings) {
            dlDrawer.closeDrawers();
            //setAppSettings();
            ((MainActivity) getActivity()).showSettingsFragment();
        } else if (v.getId() == R.id.ll_wallet_drawer_header_walllet_settings) {
            dlDrawer.closeDrawers();
            setSettings();
        } else if (v.getId() == R.id.ll_wallet_drawer_header_walllet_transactions) {
            dlDrawer.closeDrawers();
            setTransactions();
        }
    }


    @Override
    public void updateWallet(Wallet wallet) {
        DBWalletsHelper.update(getActivity(), wallet);
        int pos = -1;
        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).equals(wallet)) {
                pos = i;
            }
        }
        if (pos > -1) {
            wallets.set(pos, wallet);
            int posf = -1;
            for (int i = 0; i < wallets.size(); i++) {
                if (wallets.get(i).equals(wallet)) {
                    posf = i;
                }
            }
            if (posf > -1) {
                wallets.set(posf, wallet);
            }
        }
        setWallet();
    }

    @Override
    public void onDeleteWallet(Wallet wallet) {
        getWallets();
    }


    @Override
    public void showNotification(int type, int text) {
        if (type == TYPE_ERROR) {
            nlNotify.showError(text);
        } else if (type == TYPE_COMPLEATE) {
            nlNotify.showCompleate(text);
        }
    }
}
