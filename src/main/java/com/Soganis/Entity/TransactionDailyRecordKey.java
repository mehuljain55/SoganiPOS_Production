package com.Soganis.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TransactionDailyRecordKey implements Serializable {

    private Date date;
    private String storeId;

    // Default constructor
    public TransactionDailyRecordKey() {
    }

    // Constructor
    public TransactionDailyRecordKey(Date date, String storeId) {
        this.date = date;
        this.storeId = storeId;
    }

    // Getters and Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    // hashCode and equals (mandatory for composite keys)
    @Override
    public int hashCode() {
        return Objects.hash(date, storeId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TransactionDailyRecordKey that = (TransactionDailyRecordKey) obj;
        return Objects.equals(date, that.date) && Objects.equals(storeId, that.storeId);
    }
}