package com.Soganis.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "order_book")
@SequenceGenerator(name = "customer_order_book_squence", sequenceName = "customer_order_book_squence", initialValue = 1, allocationSize = 1)
public class CustomerOrderBook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_order_book_squence")
    private int orderId;
    private String customerName;
    private String mobileNo;

    @OneToMany(mappedBy = "customerOrderBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order> orders;

    @Temporal(TemporalType.DATE)
    private Date deliveryDate;
    
    private int amount_due;
    private int totalAmount;
    private int advancePayment;
    
    private String status;
    private  String storeId;

    public CustomerOrderBook() {
    }

    public CustomerOrderBook(int orderId, String customerName, String mobileNo, List<Order> orders, Date deliveryDate, int amount_due, int totalAmount, int advancePayment, String status, String storeId) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.mobileNo = mobileNo;
        this.orders = orders;
        this.deliveryDate = deliveryDate;
        this.amount_due = amount_due;
        this.totalAmount = totalAmount;
        this.advancePayment = advancePayment;
        this.status = status;
        this.storeId = storeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(int advancePayment) {
        this.advancePayment = advancePayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(int amount_due) {
        this.amount_due = amount_due;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
