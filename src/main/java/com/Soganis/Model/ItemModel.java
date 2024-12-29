package com.Soganis.Model;



public class ItemModel {
   
  private String schoolCode;
  private String itemCode;
  private String size;
  private String itemColor;
  private int currentQuantity;
  private int quantity;
  private  String storeId;

    public ItemModel() {
    }

    public ItemModel(String schoolCode, String itemCode, String size, String itemColor) {
        this.schoolCode = schoolCode;
        this.itemCode = itemCode;
        this.size = size;
        this.itemColor = itemColor;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
