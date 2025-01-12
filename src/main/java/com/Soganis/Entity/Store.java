package com.Soganis.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="store")
public class Store {
    @Id
    private String storeId;
    private String storeName;
    private String mobileNo;
    private String address;
    private int openingCash;

    public Store(String storeId, String storeName, String address) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
    }

    public Store() {
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getOpeningCash() {
        return openingCash;
    }

    public void setOpeningCash(int openingCash) {
        this.openingCash = openingCash;
    }


}
