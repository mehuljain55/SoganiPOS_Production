package com.Soganis.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "user_cash_collection")
@IdClass(UserCashCollectionId.class)
public class UserCashCollection {

    @Id
    private String userId;

    @Id
    @Temporal(TemporalType.DATE)
    private Date collection_date;

    private String userName;
    private int cash_collection;
    private int cash_return;
    private int final_cash_collection;
    

    // Default constructor
    public UserCashCollection() {}

    // Parameterized constructor
    public UserCashCollection(String userId, Date collection_date, String userName, int cash_collection) {
        this.userId = userId;
        this.collection_date = collection_date;
        this.userName = userName;
        this.cash_collection = cash_collection;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCash_collection() {
        return cash_collection;
    }

    public void setCash_collection(int cash_collection) {
        this.cash_collection = cash_collection;
    }

    public int getCash_return() {
        return cash_return;
    }

    public void setCash_return(int cash_return) {
        this.cash_return = cash_return;
    }

    public int getFinal_cash_collection() {
        return final_cash_collection;
    }

    public void setFinal_cash_collection(int final_cash_collection) {
        this.final_cash_collection = final_cash_collection;
    }
    
    
}
