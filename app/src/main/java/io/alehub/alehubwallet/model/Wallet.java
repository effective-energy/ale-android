package io.alehub.alehubwallet.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dima on 1/30/18.
 */

public class Wallet implements Serializable {

    private long id;
    private String name;
    private long balance;
    private String publicKey;

    public Wallet() {
    }

    public Wallet(long id, String name, long balance, String publicKey) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.publicKey = publicKey;
    }

    public Wallet(String name, long balance) {
        this.name = name;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wallet) {
            Wallet w = (Wallet) obj;
            return w.getPublicKey().equals(publicKey);
        }
        return super.equals(obj);
    }
}
