package io.alehub.alehubwallet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.model.Wallet;

/**
 * Created by dima on 2/22/18.
 */

public class DBWalletsHelper {

    public static Wallet get(long id, Context c) {
        try {
            Wallet wallet = new Wallet();
            SQLiteDatabase db = DBWorker.getDB(c);
            Cursor rs = db.rawQuery("SELECT * FROM Wallets WHERE `id`=" + id + " ;", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                wallet = fromCursor(rs);
                break;
            }
            rs.close();
            return wallet;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return null;
        }
    }

    public static Wallet getByPublicKey(String publicKey, Context c) {
        try {
            Wallet wallet = new Wallet();
            SQLiteDatabase db = DBWorker.getDB(c);
            Cursor rs = db.rawQuery("SELECT * FROM Wallets WHERE `publicKey`='" + publicKey + "' ;", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                wallet = fromCursor(rs);
                break;
            }
            rs.close();
            return wallet;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return null;
        }
    }

    public static List<Wallet> getAll(Context context){
        try{
            List<Wallet> list = new ArrayList<>();
            SQLiteDatabase db = DBWorker.getDB(context);
            Cursor rs = db.rawQuery("SELECT * FROM Wallets;", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                list.add(fromCursor(rs));
                rs.moveToNext();
            }
            rs.close();
            return list;
        }catch (Exception e){
            Log.e("App", "db", e);
            return  new ArrayList<>();
        }
    }

    public static Wallet add(Context context, Wallet wallet){
        try {
            SQLiteDatabase db = DBWorker.getDB(context);
            ContentValues values = new ContentValues();
            values.put("name",wallet.getName());
            values.put("balance",wallet.getBalance());
            values.put("publicKey",wallet.getPublicKey());
            long insertedId= db.insert("Wallets", "", values) ;
            wallet.setId(insertedId);
            return wallet;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return null;
        }
    }

    public static Wallet update(Context context, Wallet wallet){
        try {
            SQLiteDatabase db = DBWorker.getDB(context);
            ContentValues values = new ContentValues();
            values.put("name",wallet.getName());
            values.put("balance",wallet.getBalance());
            values.put("publicKey",wallet.getPublicKey());
            db.update("Wallets", values,"publicKey='"+wallet.getPublicKey()+"'",new String[]{});

            return wallet;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return null;
        }
    }

    private static Wallet fromCursor(Cursor rs) {
        Wallet wallet = new Wallet();
        wallet.setPublicKey(rs.getString(rs.getColumnIndex("publicKey")));
        wallet.setName(rs.getString(rs.getColumnIndex("name")));
        wallet.setBalance(rs.getLong(rs.getColumnIndex("balance")));
        wallet.setId(rs.getLong(rs.getColumnIndex("id")));
        return wallet;
    }

}
