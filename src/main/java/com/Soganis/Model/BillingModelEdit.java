package com.Soganis.Model;

import com.Soganis.Entity.Billing;

import java.util.List;

public class BillingModelEdit {
    private  int billNo;
    private Billing billing;
    List<ItemReturnModel> itemReturnModel;
    private TransactionModel transactionModel;
    private String storeId;

    public BillingModelEdit(int billNo, Billing billing, List<ItemReturnModel> itemReturnModel, TransactionModel transactionModel, String storeId) {
        this.billNo = billNo;
        this.billing = billing;
        this.itemReturnModel = itemReturnModel;
        this.transactionModel = transactionModel;
        this.storeId = storeId;
    }

    public BillingModelEdit() {
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public List<ItemReturnModel> getItemReturnModel() {
        return itemReturnModel;
    }

    public void setItemReturnModel(List<ItemReturnModel> itemReturnModel) {
        this.itemReturnModel = itemReturnModel;
    }

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    public void setTransactionModel(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
