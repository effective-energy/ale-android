package io.alehub.alehubwallet.network;

import android.os.AsyncTask;
import android.util.Log;

import ru.dimzhur.alehublogictester.network.ServerRequestWorker;

/**
 * Created by dima on 2/22/18.
 */

public class DownloadBackupHelper extends AsyncTask<Void, Void, Boolean> {

    private OnDownloadBackupListener listener;

    public DownloadBackupHelper(OnDownloadBackupListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            ServerRequestWorker.downloadBackup();
            return true;
        } catch (Exception e) {
            Log.e("App", "e", e);
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (listener != null) {
            listener.onDownloadBackup(aBoolean);
        }
    }

    public interface OnDownloadBackupListener {
        void onDownloadBackup(boolean status);
    }
}
