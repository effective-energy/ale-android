package io.alehub.alehubwallet.network;

import android.os.AsyncTask;
import android.util.Log;

import ru.dimzhur.alehublogictester.network.ServerRequestWorker;

/**
 * Created by dima on 2/21/18.
 */

public class UploadBackupHelper extends AsyncTask<Void, Void, Boolean> {

    private OnUploadBackupListener listener;

    public UploadBackupHelper(OnUploadBackupListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

            return ServerRequestWorker.uploadBackup();
        } catch (Exception e) {
            Log.e("app", "err", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (listener != null) {
            listener.onUploadBackup(aBoolean);
        }
    }

    public interface OnUploadBackupListener {
        void onUploadBackup(boolean done);
    }
}
