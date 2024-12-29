package com.Soganis.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="InventoryUpdateHistory")
@SequenceGenerator(name = "inventory_update_sequence", sequenceName = "inventory_update_sequence", initialValue = 1, allocationSize = 1)
public class InventoryUpdateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_update_sequence")
    private int sno;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String school;
    private String itemList;
    private String storeId;

    @OneToMany(mappedBy = "inventoryUpdateHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InventoryUpdateList> inventoryUpdateLists;


    public InventoryUpdateHistory(int sno, Date date, String school, String itemList) {
        this.sno = sno;
        this.date = date;
        this.school = school;
        this.itemList = itemList;
    }

    public InventoryUpdateHistory() {
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<InventoryUpdateList> getInventoryUpdateLists() {
        return inventoryUpdateLists;
    }

    public void setInventoryUpdateLists(List<InventoryUpdateList> inventoryUpdateLists) {
        this.inventoryUpdateLists = inventoryUpdateLists;
    }
}
