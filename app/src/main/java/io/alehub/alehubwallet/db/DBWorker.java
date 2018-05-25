package io.alehub.alehubwallet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by dima on 2/21/18.
 */

public class DBWorker {

    private static SQLiteDatabase myDb;
    private Context context;

    private DBWorker(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        //context.deleteDatabase("user.db");
        myDb = context.openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
        createTables();
    }

    public static SQLiteDatabase getDB(Context... c) {
        if (myDb == null) {
            if (c != null && c.length > 0 && c[0] != null) {
                new DBWorker(c[0]);
            }
        }
        return myDb;
    }

    private void createTables() {
        try {
            String sql = "CREATE TABLE   IF NOT EXISTS  `Wallets`  "
                    + "(`id` INTEGER PRIMARY KEY     autoincrement,"
                    + " `name`           TEXT, "
                    + " `balance`           INT    NOT NULL, "
                    + " `publicKey`            TEXT     NOT NULL)";
            myDb.execSQL(sql);
            sql = "CREATE TABLE  IF NOT EXISTS   `Transactions` "
                    + "(`id` INTEGER PRIMARY KEY     autoincrement,"
                    + " `date`           INT    NOT NULL, "
                    + " `from`           TEXT    NOT NULL, "
                    + " `to`           TEXT    NOT NULL, "
                    + " `count`            INT     NOT NULL)";
            myDb.execSQL(sql);
        } catch (Exception e) {
            Log.e("App", "Create user tables", e);
        }

    }
}
