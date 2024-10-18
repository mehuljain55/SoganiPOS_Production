package com.Soganis.Model;

public class ItemAddInventoryModel {

  private String schoolCode;
  private String itemCode;
  private String itemColor;

    public ItemAddInventoryModel(String schoolCode, String itemCode, String itemColor) {
        this.schoolCode = schoolCode;
        this.itemCode = itemCode;
        this.itemColor = itemColor;
    }

    public ItemAddInventoryModel() {
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

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    @Override
    public String toString() {
        return "ItemAddInventoryModel{" + "schoolCode=" + schoolCode + ", itemCode=" + itemCode + ", itemColor=" + itemColor + '}';
    }
  
  
  
    
    
}
