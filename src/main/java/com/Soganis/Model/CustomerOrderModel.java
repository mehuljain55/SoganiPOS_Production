package com.Soganis.Model;

import com.Soganis.Entity.CustomerOrderBook;


public class CustomerOrderModel {
    private CustomerOrderBook customerOrderBook;
    private  String storeId;


    public CustomerOrderModel(CustomerOrderBook customerOrderBook, String storeId) {
        this.customerOrderBook = customerOrderBook;
        this.storeId = storeId;
    }

    public CustomerOrderModel() {
    }

    public CustomerOrderBook getCustomerOrderBook() {
        return customerOrderBook;
    }

    public void setCustomerOrderBook(CustomerOrderBook customerOrderBook) {
        this.customerOrderBook = customerOrderBook;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
