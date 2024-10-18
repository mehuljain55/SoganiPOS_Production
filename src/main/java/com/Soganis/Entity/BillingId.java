package com.Soganis.Entity;

import java.io.Serializable;
import java.util.Objects;

public class BillingId implements Serializable {
    private int billNo;
    private String storeId;

    // Constructors, Getters, Setters, hashCode, and equals

    public BillingId() {}

    public BillingId(int billNo, String storeId) {
        this.billNo = billNo;
        this.storeId = storeId;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingId that = (BillingId) o;
        return billNo == that.billNo && Objects.equals(storeId, that.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billNo, storeId);
    }
}
