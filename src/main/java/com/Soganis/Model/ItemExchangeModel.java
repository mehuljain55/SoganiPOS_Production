package com.Soganis.Model;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.User;

import java.util.List;


public class ItemExchangeModel {

    private int billNo;
    private Billing bill;
    private List<ItemReturnModel> itemModel;
    private User user;

    public Billing getBill() {
        return bill;
    }

    public void setBill(Billing bill) {
        this.bill = bill;
    }

    public List<ItemReturnModel> getItemModel() {
        return itemModel;
    }

    public void setItemModel(List<ItemReturnModel> itemModel) {
        this.itemModel = itemModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }
}
