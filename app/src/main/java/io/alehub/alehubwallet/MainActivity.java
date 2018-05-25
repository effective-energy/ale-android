package io.alehub.alehubwallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.alehub.alehubwallet.fragment.NotificationsFragment;
import io.alehub.alehubwallet.fragment.RequestFragment;
import io.alehub.alehubwallet.fragment.SendFragment;
import io.alehub.alehubwallet.fragment.WalletFragment;
import io.alehub.alehubwallet.network.DownloadBackupHelper;
import io.alehub.alehubwallet.view.TextViewFont;

/**
 * Created by dima on 1/24/18.
 */

public class MainActivity extends LangBaseActivity implements TabLayout.OnTabSelectedListener {


    private TabLayout tlTabs;


    private int curTab = 0;

    private int[] tabIcons = {R.drawable.ic_wallet, R.drawable.ic_send_white, R.drawable.ic_receive_white, R.drawable.ic_notifications};
    private int[] tabTitles = {R.string.wallet_title, R.string.send_title, R.string.recived_title, R.string.notifications_title};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tlTabs = (TabLayout) findViewById(R.id.tl_main_tabs);
        tlTabs.addOnTabSelectedListener(this);
        initTabs();
        setFragment(curTab);
        App.run = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        new DownloadBackupHelper(null).execute();
        App.run = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.run = true;
    }

    private void setFragment(int pos) {
        if (pos == 0) {
            showWalletFragment();
        } else if (pos == 1) {
            showSendFragment();
        } else if (pos == 2) {
            showReciveFragment();
        } else if (pos == 3) {
            setNotifyFragment();
        }
    }


    private void initTabs() {
        tlTabs.removeAllTabs();
        int i = 0;
        for (int res : tabIcons) {
            TabLayout.Tab tab = tlTabs.newTab();
            View v = LayoutInflater.from(this).inflate(R.layout.tab, null);
            AppCompatImageView iv = (AppCompatImageView) v.findViewById(R.id.iv_tab_icon);
            TextViewFont tv = (TextViewFont)v.findViewById(R.id.tv_tab_title);
            tv.setText(tabTitles[i]);
            iv.setImageDrawable(VectorDrawableCompat.create(getResources(), res, null));
            if (i == curTab) {
                v.setBackgroundResource(R.color.grey);
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            v.setLayoutParams(lp);
            tab.setCustomView(v);
            tlTabs.addTab(tab);
            i++;
        }
        tlTabs.getTabAt(curTab).select();

    }

    public void showSendFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new SendFragment())
                .commit();
    }

    public void showReciveFragment() {
        RequestFragment rf = new RequestFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, rf)
                .commit();
    }

    public void setNotifyFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new NotificationsFragment())
                .commit();
    }

    public void showSettingsFragment() {
        startActivityForResult(new Intent(this, AppSettingsActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void showWalletFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new WalletFragment())
                .commit();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.getCustomView().setBackgroundResource(R.color.grey);
        if (tab.getPosition() != curTab) {
            setFragment(tab.getPosition());
        }
        curTab = tab.getPosition();
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
            if (tlTabs.getSelectedTabPosition() == 0) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.close_app)
                        .setMessage(R.string.close_app_ask)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton(R.string.no, null)
                        .show();
            } else {
                tlTabs.getTabAt(0).select();
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getCustomView().setBackgroundResource(R.color.colorPrimary);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
