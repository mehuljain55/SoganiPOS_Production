package com.Soganis.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
@SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", initialValue = 1, allocationSize = 1)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private int sno;
    private String schoolName;
    private String itemType;
    private String description;
    private String size;
    private int quantity;
    private int price;
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_book_id")
    @JsonBackReference
    private CustomerOrderBook customerOrderBook;

    public Order() {
    }

    public Order(int sno, String schoolName, String itemType, String description, String size, int quantity, int price, int amount, CustomerOrderBook customerOrderBook) {
        this.sno = sno;
        this.schoolName = schoolName;
        this.itemType = itemType;
        this.description = description;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
        this.customerOrderBook = customerOrderBook;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CustomerOrderBook getCustomerOrderBook() {
        return customerOrderBook;
    }

    public void setCustomerOrderBook(CustomerOrderBook customerOrderBook) {
        this.customerOrderBook = customerOrderBook;
    }
    
    

   
    

}
 