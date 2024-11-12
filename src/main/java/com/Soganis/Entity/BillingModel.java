package com.Soganis.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "billing_tab")
@SequenceGenerator(name = "billing_model_sequence", sequenceName = "billing_model_sequence", initialValue = 1, allocationSize = 1)
public class BillingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_model_sequence")
    private int sno;
    private String itemBarcodeID;
    private String itemType;
    private String itemColor;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date bill_date;
    private String itemSize;
    private String itemCategory;
    private String billCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "bill_no", referencedColumnName = "billNo"),
            @JoinColumn(name = "store_id", referencedColumnName = "storeId")
    })
    @JsonIgnore
    private Billing billing;
    private int sellPrice;
    private int price;

    
    private int quantity;
    private int total_amount;
    private int final_amount;
    private String status;
    private int discount;
    private String storeName;

    public BillingModel() {
    }

    public BillingModel(int sno, String itemBarcodeID, String itemType, String itemColor, String itemSize, String itemCategory, Billing billing, int sellPrice, int quantity) {
        this.sno = sno;
        this.itemBarcodeID = itemBarcodeID;
        this.itemType = itemType;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.itemCategory = itemCategory;
        this.billing = billing;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getItemBarcodeID() {
        return itemBarcodeID;
    }

    public void setItemBarcodeID(String itemBarcodeID) {
        this.itemBarcodeID = itemBarcodeID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBill_date() {
        return bill_date;
    }

    public void setBill_date(Date bill_date) {
        this.bill_date = bill_date;
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

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscount() {
        return discount;
    }


    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(String billCategory) {
        this.billCategory = billCategory;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(int final_amount) {
        this.final_amount = final_amount;
    }
    
    
    

    


    @Override
    public String toString() {
        return "BillingModel{" +
                "sno=" + sno +
                ", itemBarcodeID='" + itemBarcodeID + '\'' +
                ", itemType='" + itemType + '\'' +
                ", itemColor='" + itemColor + '\'' +
                ", description='" + description + '\'' +
                ", bill_date=" + bill_date +
                ", itemSize='" + itemSize + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", billing=" + billing +
                ", sellPrice=" + sellPrice +
                ", quantity=" + quantity +
                ", total_amount=" + total_amount +
                ", status='" + status + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
