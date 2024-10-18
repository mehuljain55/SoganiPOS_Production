package com.Soganis.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user")
public class User {

    @Id
    private String userId;
    private String sname;
    private String mobile_no;
    private String password;
    private int monthly_salary;
    private int payable_balance;
    private  String storeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMonthly_salary() {
        return monthly_salary;
    }

    public void setMonthly_salary(int monthly_salary) {
        this.monthly_salary = monthly_salary;
    }

    public int getPayable_balance() {
        return payable_balance;
    }

    public void setPayable_balance(int payable_balance) {
        this.payable_balance = payable_balance;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
