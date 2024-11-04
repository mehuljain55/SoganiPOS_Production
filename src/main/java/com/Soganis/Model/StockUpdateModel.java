package com.Soganis.Model;


public class StockUpdateModel {
  
     private String itemCode;
     private int qty;
     private String storeId;

    public StockUpdateModel(String itemCode, int qty, String storeId) {
        this.itemCode = itemCode;
        this.qty = qty;
        this.storeId = storeId;
    }

    public StockUpdateModel() {
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    
     
    
}
