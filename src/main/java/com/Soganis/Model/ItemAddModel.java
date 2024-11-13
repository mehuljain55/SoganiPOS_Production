package com.Soganis.Model;


public class ItemAddModel {
   private String barcodedId;    
   private int price;
   private int wholeSalePrice;
   private int quantity;

    public String getBarcodedId() {
        return barcodedId;
    }

    public void setBarcodedId(String barcodedId) {
        this.barcodedId = barcodedId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(int wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }
}
