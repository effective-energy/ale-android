package io.alehub.alehubwallet.model;

/**
 * Created by dima on 1/25/18.
 */

public class BackupCode {

    private String code;
    private boolean used;
    private long id;

    public BackupCode() {
    }

    public BackupCode(String code, boolean used, long id) {
        this.code = code;
        this.used = used;
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
