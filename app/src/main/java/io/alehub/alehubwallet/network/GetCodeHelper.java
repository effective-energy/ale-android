package io.alehub.alehubwallet.network;

import android.os.AsyncTask;

import ru.dimzhur.alehublogictester.network.ServerRequestWorker;

/**
 * Created by dima on 2/21/18.
 */

public class GetCodeHelper extends AsyncTask<String, Void, String> {

    private OnGetCodeListener listener;

    public GetCodeHelper(OnGetCodeListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String cd = "";
        if (strings != null && strings.length > 0 && strings[0] != null) {
            cd = strings[0];
        }
        return ServerRequestWorker.getCode(cd);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (listener != null) {
            listener.onGetCode(s);
        }
    }

    public interface OnGetCodeListener {
        void onGetCode(String code);
    }
}
