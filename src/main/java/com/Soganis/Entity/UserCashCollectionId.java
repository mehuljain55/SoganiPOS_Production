package com.Soganis.Entity;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class UserCashCollectionId implements Serializable {
    private String userId;
    private Date collection_date;

    // Default constructor
    public UserCashCollectionId() {}

    // Parameterized constructor
    public UserCashCollectionId(String userId, Date collection_date) {
        this.userId = userId;
        this.collection_date = collection_date;
    }

    // Getters and setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(Date collection_date) {
        this.collection_date = collection_date;
    }

    // hashCode and equals methods
    @Override
    public int hashCode() {
        return Objects.hash(userId, collection_date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserCashCollectionId that = (UserCashCollectionId) obj;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(collection_date, that.collection_date);
    }
}
