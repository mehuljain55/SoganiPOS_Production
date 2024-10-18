package com.Soganis.Model;


import com.Soganis.Entity.PurchaseOrderBook;
import com.Soganis.Entity.User;

import java.util.List;

public class PurchaseOrderModel {
   
    private  List<PurchaseOrderBook> purchaseOrderBookList;
    private User user;

    public PurchaseOrderModel(List<PurchaseOrderBook> purchaseOrderBookList, User user) {
        this.purchaseOrderBookList = purchaseOrderBookList;
        this.user = user;
    }

    public PurchaseOrderModel() {
    }

    public List<PurchaseOrderBook> getPurchaseOrderBookList() {
        return purchaseOrderBookList;
    }

    public void setPurchaseOrderBookList(List<PurchaseOrderBook> purchaseOrderBookList) {
        this.purchaseOrderBookList = purchaseOrderBookList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
