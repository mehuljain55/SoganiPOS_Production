package com.Soganis.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="inter_company_payment_record")
@SequenceGenerator(name = "inter_company_sequence", sequenceName = "inter_company_sequence", initialValue = 1, allocationSize = 1)

public class InterCompanyPayments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inter_company_sequence")
     private int sno;
    private  String description;
    private int amount;
    private String status;
    private Date date;
    private String billTo;
    private  String billBy;
    private String type;
    private  String store_id;

    public InterCompanyPayments(int sno, String description, int amount, String status, Date date, String billTo, String billBy, String store_id) {
        this.sno = sno;
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.date = date;
        this.billTo = billTo;
        this.billBy = billBy;
        this.store_id = store_id;
    }

    public InterCompanyPayments() {
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getBillBy() {
        return billBy;
    }

    public void setBillBy(String billBy) {
        this.billBy = billBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
