package io.alehub.alehubwallet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.alehub.alehubwallet.R;
import io.alehub.alehubwallet.network.GetCodeHelper;
import io.alehub.alehubwallet.network.StartWalletHelper;
import io.alehub.alehubwallet.network.UploadBackupHelper;
import io.alehub.alehubwallet.util.SettingsHelper;
import ru.dimzhur.alehublogictester.network.Config;

/**
 * Created by dima on 2/21/18.
 */

public class InitFragment extends Fragment implements GetCodeHelper.OnGetCodeListener, UploadBackupHelper.OnUploadBackupListener, StartWalletHelper.OnStartWalletListener {

    private OnInitDoneListener doneListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wait_screen, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));




        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startInit();
    }

    public void startInit() {
        new GetCodeHelper(this).execute(SettingsHelper.getCode(getActivity()));
    }

    public void uploadBackup() {
        new UploadBackupHelper(this).execute();
    }

    public void setDoneListener(OnInitDoneListener doneListener) {
        this.doneListener = doneListener;
    }

    @Override
    public void onGetCode(String code) {
        if (code != null) {
            SettingsHelper.setCode(getActivity(), code);
            Config.code = code;
            uploadBackup();
        }
    }

    @Override
    public void onUploadBackup(boolean done) {
        new StartWalletHelper(this).execute();
    }

    @Override
    public void onWalletStart(boolean done) {
        if (doneListener != null) {
            doneListener.onInitDone();
        }
    }

    public interface OnInitDoneListener {
        void onInitDone();
    }


    private static void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init((KeyManager[])null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            Config.selfSignedSSL =  sc.getSocketFactory();
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
        } catch (KeyManagementException var4) {
            var4.printStackTrace();
        }

    }

    static {
        disableSslVerification();
    }

}
