package io.alehub.alehubwallet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.dimzhur.alehublogictester.network.ServerRequestWorker;

/**
 * Created by dima on 2/22/18.
 */

public class BackupService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        backupThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private Thread backupThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (Exception e) {

                }
                try {
                    ServerRequestWorker.downloadBackup();
                } catch (Exception e) {
                    Log.e("App", "e", e);
                }
                try {
                    Thread.sleep(1000 * 60 * 5);
                } catch (Exception e) {

                }
            }
        }
    });
}
