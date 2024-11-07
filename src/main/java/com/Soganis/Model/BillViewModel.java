package com.Soganis.Model;

import com.Soganis.Entity.Billing;

import java.util.List;

public class BillViewModel {
    private String type;          // Message to provide additional info
    private List<Billing> billList;     // List of bills (can be a single item or multiple)
    private Billing bill;

    public BillViewModel(String type, List<Billing> billList, Billing bill) {
        this.type = type;
        this.billList = billList;
        this.bill = bill;
    }

    public BillViewModel(String type, List<Billing> billList) {
        this.type = type;
        this.billList = billList;
    }

    public BillViewModel(String type, Billing bill) {
        this.type = type;
        this.bill = bill;
    }

    public BillViewModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Billing> getBillList() {
        return billList;
    }

    public void setBillList(List<Billing> billList) {
        this.billList = billList;
    }

    public Billing getBill() {
        return bill;
    }

    public void setBill(Billing bill) {
        this.bill = bill;
    }
}
