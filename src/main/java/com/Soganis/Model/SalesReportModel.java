package com.Soganis.Model;

public class SalesReportModel {

    private String itemBarcodeID;
    private String itemCode;
    
    private String description;
    private String itemType;
    private String itemColor;
    private String itemSize;
    
    private int sellPrice;    
    private int totalQuantity;
    private int totalAmount;

    public SalesReportModel(String itemBarcodeID, String description, String itemType, String itemColor, int sellPrice, int totalQuantity, int totalAmount) {
        this.itemBarcodeID = itemBarcodeID;
        this.description = description;
        this.itemType = itemType;
        this.itemColor = itemColor;
        this.sellPrice = sellPrice;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public SalesReportModel() {
    }

    public String getItemBarcodeID() {
        return itemBarcodeID;
    }

    public void setItemBarcodeID(String itemBarcodeID) {
        this.itemBarcodeID = itemBarcodeID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }
    
    
    
    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    
    
    
    
  
  
    

}
