package com.Soganis.Model;

public class ItemEditModel {

    private String itemBarcodeID;
    private String itemCode;
    private String itemName;
    private String description;
    private String itemType;
    private String itemColor;
    private String itemSize;
    private String itemCategory;
    private int price;
    private int wholeSalePrice;
    private int quantity;
    private  String storeId;

    public ItemEditModel(String itemBarcodeID, String itemCode, String itemName, String description, String itemType, String itemColor, String itemSize, String itemCategory, int price, int wholeSalePrice, int quantity, String storeId) {
        this.itemBarcodeID = itemBarcodeID;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.description = description;
        this.itemType = itemType;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.itemCategory = itemCategory;
        this.price = price;
        this.wholeSalePrice = wholeSalePrice;
        this.quantity = quantity;
        this.storeId = storeId;
    }

    public ItemEditModel() {
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(int wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
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
