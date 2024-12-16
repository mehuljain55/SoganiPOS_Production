package com.Soganis.Entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="transaction_due_retial")
@SequenceGenerator(name = "transaction_due_sequence", sequenceName = "transaction_due_sequence", initialValue = 1, allocationSize = 1)

public class TransactionDueListRetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_due_sequence")
    private int sno;
    private String customerName;
    private String customerMobileNo;
    @Temporal(TemporalType.DATE)
    private Date date;
    private int billNo;
    private int amount;
    private String storeId;
    private String status;

    public TransactionDueListRetail(int sno, String customerName, String customerMobileNo, Date date, int billNo, int amount, String storeId, String status) {
        this.sno = sno;
        this.customerName = customerName;
        this.customerMobileNo = customerMobileNo;
        this.date = date;
        this.billNo = billNo;
        this.amount = amount;
        this.storeId = storeId;
        this.status = status;
    }

    public TransactionDueListRetail() {
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
