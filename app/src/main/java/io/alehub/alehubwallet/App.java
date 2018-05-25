package io.alehub.alehubwallet;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

import ru.dimzhur.alehublogictester.network.Config;

/**
 * Created by dima on 1/23/18.
 */

public class App extends Application {

    private Context context;

    public static boolean run = false;


    @Override
    public void onCreate() {
        super.onCreate();

        Config.BASE_DIR = getFilesDir().getAbsolutePath() + "/";
        Config.BASE_PORT = 1111;
        Config.BASE_URL = "https://54.208.222.239";
        Config.id = "ALEHUB_WALLET_TEST";
        try {
          //  Config.selfSignedSSL = SelfSignedSSLSocketFactory.buildSslSocketFactory(new File("hui"));
        } catch (Exception e) {

        }

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;

            }

            ;
        };


        Log.e("Config", Config.getConfig());
        context = this;
        startService(new Intent(this, BackupService.class));

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
