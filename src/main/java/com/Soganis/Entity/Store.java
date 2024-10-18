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

    public Store(String storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
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
}
