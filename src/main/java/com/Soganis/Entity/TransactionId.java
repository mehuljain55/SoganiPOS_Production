package com.Soganis.Entity;

import java.util.Objects;

public class TransactionId {
    private int transactionId;
    private String storeId;

    // Default constructor
    public TransactionId() {}

    // Constructor
    public TransactionId(int transactionId, String storeId) {
        this.transactionId = transactionId;
        this.storeId = storeId;
    }

    // Getters and setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionId that = (TransactionId) o;
        return transactionId == that.transactionId && Objects.equals(storeId, that.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, storeId);
    }
}
