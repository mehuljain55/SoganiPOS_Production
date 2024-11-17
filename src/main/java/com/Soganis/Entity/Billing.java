package com.Soganis.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "billing")
@IdClass(BillingId.class)
public class Billing {

    @Id
    private int billNo;

    @Id
    private String storeId;

    String userId;
    @Temporal(TemporalType.DATE)
    private Date bill_date;
    String customerName;
    String customerMobileNo;
    String paymentMode;
    String description;
    String schoolName;
    String billType;
    int discount;
    int discountAmount;
    
    int balanceAmount;
    int item_count;

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL)
    private List<BillingModel> bill;
    int final_amount;

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBill_date() {
        return bill_date;
    }

    public void setBill_date(Date bill_date) {
        this.bill_date = bill_date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public int getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(int balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public List<BillingModel> getBill() {
        return bill;
    }

    public void setBill(List<BillingModel> bill) {
        this.bill = bill;
    }

    public int getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(int final_amount) {
        this.final_amount = final_amount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return "Billing{" +
                "billNo=" + billNo +
                ", storeId='" + storeId + '\'' +
                ", userId='" + userId + '\'' +
                ", bill_date=" + bill_date +
                ", customerName='" + customerName + '\'' +
                ", customerMobileNo='" + customerMobileNo + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", description='" + description + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", billType='" + billType + '\'' +
                ", discount=" + discount +
                ", discountAmount=" + discountAmount +
                ", balanceAmount=" + balanceAmount +
                ", item_count=" + item_count +
                ", final_amount=" + final_amount +
                '}';
    }
}
