package com.Soganis.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name="InventoryUpdateList")
@SequenceGenerator(name = "inventory_update_list_sequence", sequenceName = "inventory_update_list_sequence", initialValue = 1, allocationSize = 1)
public class InventoryUpdateList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_update_list_sequence")
    private int sno;
    private String itemCode;
    private String description;
    private  int quantity;
    private String storeId;

    @ManyToOne
    @JoinColumn(name = "inventory_update_history_id", nullable = false) // Foreign key column in the InventoryUpdateList table
    @JsonBackReference
    private InventoryUpdateHistory inventoryUpdateHistory;

    public InventoryUpdateList(String itemCode, String description, int quantity, String storeId) {
        this.itemCode = itemCode;
        this.description = description;
        this.quantity = quantity;
        this.storeId = storeId;
    }

    public InventoryUpdateList() {
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public InventoryUpdateHistory getInventoryUpdateHistory() {
        return inventoryUpdateHistory;
    }

    public void setInventoryUpdateHistory(InventoryUpdateHistory inventoryUpdateHistory) {
        this.inventoryUpdateHistory = inventoryUpdateHistory;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
