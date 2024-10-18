package com.Soganis.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_monthly_salary")
@IdClass(UserMonthlySalaryId.class)
public class UserMonthlySalary {

    @Id
    private String userId;
    
    @Id
    private String month_fy;
    private String user_name;
    private int monthlySalary;
    private int salaryDeducted;
    private int advanceSalary;    
    private int finalAmount;
    private String status;
    private  String storeId;

    public UserMonthlySalary(String userId, String month_fy, String user_name, int monthlySalary, int salaryDeducted, int advanceSalary, int finalAmount, String status, String storeId) {
        this.userId = userId;
        this.month_fy = month_fy;
        this.user_name = user_name;
        this.monthlySalary = monthlySalary;
        this.salaryDeducted = salaryDeducted;
        this.advanceSalary = advanceSalary;
        this.finalAmount = finalAmount;
        this.status = status;
        this.storeId = storeId;
    }

    public UserMonthlySalary() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMonth_fy() {
        return month_fy;
    }

    public void setMonth_fy(String month_fy) {
        this.month_fy = month_fy;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(int monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public int getSalaryDeducted() {
        return salaryDeducted;
    }

    public void setSalaryDeducted(int salaryDeducted) {
        this.salaryDeducted = salaryDeducted;
    }

    public int getAdvanceSalary() {
        return advanceSalary;
    }

    public void setAdvanceSalary(int advanceSalary) {
        this.advanceSalary = advanceSalary;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "UserMonthlySalary{" +
                "userId='" + userId + '\'' +
                ", month_fy='" + month_fy + '\'' +
                ", user_name='" + user_name + '\'' +
                ", monthlySalary=" + monthlySalary +
                ", salaryDeducted=" + salaryDeducted +
                ", advanceSalary=" + advanceSalary +
                ", finalAmount=" + finalAmount +
                ", status='" + status + '\'' +
                ", storeId='" + storeId + '\'' +
                '}';
    }
}
