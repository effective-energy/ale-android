package io.alehub.alehubwallet.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.dimzhur.alehublogictester.network.walletresponse.TransactionResponse;

/**
 * Created by dima on 2/26/18.
 */

public class Transaction implements Serializable {

    public static final int TYPE_SENT = 1;
    public static final int TYPE_SOLD = 2;
    public static final int TYPE_RECIVED = 3;
    public static final int TYPE_BOUGHT = 4;


    private long id;
    private long date;
    private String from;
    private String to;
    private long count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public int getType(String wallet) {
        if (from.equals(wallet)) {
            return TYPE_SENT;
        } else if (to.equals(wallet)) {
            return TYPE_RECIVED;
        } else {
            return -1;
        }

    }

    public static Transaction fromResponse(TransactionResponse response) {
        Transaction tr = new Transaction();
        tr.setTo(response.getReceiver());
        tr.setFrom(response.getSender());
        tr.setDate(response.getDate().getTime());
        tr.setCount(response.getAmount());
        return tr;
    }

    public static List<Transaction> fromResponses(List<TransactionResponse> responses) {
        List<Transaction> transactions = new ArrayList<>();
        if (responses != null) {
            for (TransactionResponse tresp : responses) {
                if (tresp != null) {
                    transactions.add(fromResponse(tresp));
                }
            }
        }
        return transactions;
    }

    public static void sortByTime(List<Transaction> transactions) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                if (o1.getDate() < o2.getDate()) {
                    return 1;
                } else return -1;
            }
        });
    }
}
