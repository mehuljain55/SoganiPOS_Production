package com.Soganis.Entity;

import java.io.Serializable;
import java.util.Objects;

public class UserMonthlySalaryId implements Serializable {

    private String userId;
    private String month_fy;
   
    

    public UserMonthlySalaryId() {
    }

    public UserMonthlySalaryId(String userId, String month_fy) {
        this.userId = userId;
        this.month_fy = month_fy;
    }

    // equals and hashCode methods are required for @IdClass

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMonthlySalaryId that = (UserMonthlySalaryId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(month_fy, that.month_fy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, month_fy);
    }

    // getters and setters

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

    @Override
    public String toString() {
        return "UserMonthlySalaryId{" + "userId=" + userId + ", month_fy=" + month_fy + '}';
    }
    
    
}
