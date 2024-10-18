package com.Soganis.Model;



public class ItemModel {
   
  private String schoolCode;
  private String itemCode;
  private String size;
  private String itemColor;
  private int quantity;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    

    @Override
    public String toString() {
        return "ItemModel{" + "schoolCode=" + schoolCode + ", itemCode=" + itemCode + ", size=" + size + ", itemColor=" + itemColor + '}';
    }
  
  
    
}
