package io.alehub.alehubwallet.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import io.alehub.alehubwallet.R;

/**
 * Created by dima on 2/21/18.
 */

public class WaitDialog {
    private TextView title;
    private AlertDialog dialog;
    private boolean show = false;

    public WaitDialog(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.wait_dialog, null);
        title = (TextView) v.findViewById(R.id.tv_wait_dialog_title);
        dialog = new AlertDialog.Builder(context)
                .setView(v)
                .setCancelable(false)
                .create();
    }

    public void show(String text){
        if(!show){
            title.setText(text);
            dialog.show();
            show = true;
        }
    }

    public void show(int text){
        if(!show){
            title.setText(text);
            dialog.show();
            show = true;
        }
    }

    public void hide(){
        if(show){
            dialog.hide();
            show = false;
        }
    }
}
