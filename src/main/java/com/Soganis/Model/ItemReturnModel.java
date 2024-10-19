package com.Soganis.Model;


public class ItemReturnModel {
  
   private int sno;
   private String barcodedId;
   private int return_quantity;
   private int price;
   private String userId;

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getBarcodedId() {
        return barcodedId;
    }

    public void setBarcodedId(String barcodedId) {
        this.barcodedId = barcodedId;
    }



    public int getReturn_quantity() {
        return return_quantity;
    }

    public void setReturn_quantity(int return_quantity) {
        this.return_quantity = return_quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
