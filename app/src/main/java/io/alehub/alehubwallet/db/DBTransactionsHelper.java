package io.alehub.alehubwallet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.alehub.alehubwallet.model.Transaction;
import io.alehub.alehubwallet.model.Wallet;

/**
 * Created by dima on 2/26/18.
 */

public class DBTransactionsHelper {

    public static Transaction get(Context context, long id) {
        try {
            Transaction transaction = new Transaction();
            SQLiteDatabase db = DBWorker.getDB(context);
            Cursor rs = db.rawQuery("SELECT * FROM `Transactions` WHERE `id`=" + id + " ;", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                transaction = fromCursor(rs);
                break;
            }
            rs.close();
            return transaction;
        } catch (Exception e) {
            Log.e("App", "DB", e);
            return null;
        }
    }

    public static Transaction get(Context context, String from, String to, long date) {
        try {
            Transaction transaction = new Transaction();
            SQLiteDatabase db = DBWorker.getDB(context);
            Cursor rs = db.rawQuery("SELECT * FROM `Transactions` WHERE `from`='" + from + "' AND `to`='" + to + "' AND `date`=" + date + " ;", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                transaction = fromCursor(rs);
                break;
            }
            rs.close();
            return transaction;
        } catch (Exception e) {
            Log.e("App", "DB", e);
            return null;
        }
    }


    public static List<Transaction> getAll(Context context) {
        try {
            List<Transaction> list = new ArrayList<>();
            SQLiteDatabase db = DBWorker.getDB(context);
            Cursor rs = db.rawQuery("SELECT * FROM `Transactions`;", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                list.add(fromCursor(rs));
                rs.moveToNext();
            }
            rs.close();
            return list;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return new ArrayList<>();
        }
    }

    public static List<Transaction> getAll(Context context, String publicKey) {
        try {
            List<Transaction> list = new ArrayList<>();
            SQLiteDatabase db = DBWorker.getDB(context);
            Cursor rs = db.rawQuery("SELECT * FROM `Transactions` WHERE `from`='" + publicKey + "' OR `to`='" + publicKey + "';", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                list.add(fromCursor(rs));
                rs.moveToNext();
            }
            rs.close();
            return list;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return new ArrayList<>();
        }
    }

    public static List<Transaction> getAll(Context context, String publicKey, int from, int count) {
        try {
            List<Transaction> list = new ArrayList<>();
            SQLiteDatabase db = DBWorker.getDB(context);
            Cursor rs = db.rawQuery("SELECT * FROM `Transactions` WHERE `from`='" + publicKey + "' OR `to`='" + publicKey + "' LIMIT " + from + ", " + count + ";", new String[]{});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                list.add(fromCursor(rs));
                rs.moveToNext();
            }
            rs.close();
            return list;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return new ArrayList<>();
        }
    }

    public static List<Transaction> getByDates(Context context, long from, long to) {
        try {
            List<Transaction> list = new ArrayList<>();
            SQLiteDatabase db = DBWorker.getDB(context);
            Cursor rs = db.rawQuery("SELECT * FROM `Transactions` WHERE `date`> ? AND `date`< ?;", new String[]{from + "", to + ""});
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                list.add(fromCursor(rs));
                rs.moveToNext();
            }
            rs.close();
            return list;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return new ArrayList<>();
        }
    }

    public static Transaction add(Context context, Transaction transaction) {
        try {
            SQLiteDatabase db = DBWorker.getDB(context);
            ContentValues values = new ContentValues();
            values.put("`count`", transaction.getCount());
            values.put("`date`", transaction.getDate());
            values.put("`from`", transaction.getFrom());
            values.put("`to`", transaction.getTo());
            long insertedId = db.insert("`Transactions`", "", values);
            transaction.setId(insertedId);
            return transaction;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return null;
        }
    }

    public static Transaction update(Context context, Transaction transaction) {
        try {
            SQLiteDatabase db = DBWorker.getDB(context);
            ContentValues values = new ContentValues();
            values.put("`count`", transaction.getCount());
            values.put("`date`", transaction.getDate());
            values.put("`from`", transaction.getFrom());
            values.put("`to`", transaction.getTo());
            db.update("`Transactions`", values, "id=" + transaction.getId(), null);
            return transaction;
        } catch (Exception e) {
            Log.e("App", "db", e);
            return null;
        }
    }

    private static Transaction fromCursor(Cursor rs) {
        Transaction transaction = new Transaction();
        transaction.setCount(rs.getLong(rs.getColumnIndex("count")));
        transaction.setDate(rs.getLong(rs.getColumnIndex("date")));
        transaction.setFrom(rs.getString(rs.getColumnIndex("from")));
        transaction.setId(rs.getLong(rs.getColumnIndex("id")));
        transaction.setTo(rs.getString(rs.getColumnIndex("to")));
        return transaction;
    }


}
