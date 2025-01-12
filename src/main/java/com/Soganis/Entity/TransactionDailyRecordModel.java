package com.Soganis.Entity;

import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Date;

@Entity
@Table(name = "Transaction_Daily_Record")
@IdClass(TransactionDailyRecordKey.class)
public class TransactionDailyRecordModel {

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    @Id
    private String storeId;

    private int openingCash;
    private int closingCash;

    public TransactionDailyRecordModel(Date date, String storeId, int openingCash, int closingCash) {
        this.date = date;
        this.storeId = storeId;
        this.openingCash = openingCash;
        this.closingCash = closingCash;
    }

    public TransactionDailyRecordModel() {
    }

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

    public int getOpeningCash() {
        return openingCash;
    }

    public void setOpeningCash(int openingCash) {
        this.openingCash = openingCash;
    }

    public int getClosingCash() {
        return closingCash;
    }

    public void setClosingCash(int closingCash) {
        this.closingCash = closingCash;
    }
}
