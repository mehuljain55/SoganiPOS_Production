package com.Soganis.Entity.GraphAnalysisModel;


import java.util.Date;

public class SalesDateModel {

    private Date salesDate;
    private int salesAmount;
    private String salesMonth;
    private String salesYear;

    public SalesDateModel(Date salesDate, int salesAmount, String salesMonth, String salesYear) {
        this.salesDate = salesDate;
        this.salesAmount = salesAmount;
        this.salesMonth = salesMonth;
        this.salesYear = salesYear;
    }

    public SalesDateModel(Date salesDate, int salesAmount) {
        this.salesDate = salesDate;
        this.salesAmount = salesAmount;
    }

    public SalesDateModel() {
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public int getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(int salesAmount) {
        this.salesAmount = salesAmount;
    }

    public String getSalesMonth() {
        return salesMonth;
    }

    public void setSalesMonth(String salesMonth) {
        this.salesMonth = salesMonth;
    }

    public String getSalesYear() {
        return salesYear;
    }

    public void setSalesYear(String salesYear) {
        this.salesYear = salesYear;
    }
}
