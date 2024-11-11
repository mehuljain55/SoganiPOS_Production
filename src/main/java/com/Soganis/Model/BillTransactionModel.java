package com.Soganis.Model;

import com.Soganis.Entity.Billing;

public class BillTransactionModel {
    private Billing billing;
    private TransactionModel transactionModel;

    public BillTransactionModel(Billing billing, TransactionModel transactionModel) {
        this.billing = billing;
        this.transactionModel = transactionModel;
    }

    public BillTransactionModel() {
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    public void setTransactionModel(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }
}
