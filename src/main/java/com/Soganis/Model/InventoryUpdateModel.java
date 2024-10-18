package com.Soganis.Model;

import com.Soganis.Entity.User;

import java.util.List;

public class InventoryUpdateModel {

    private List<ItemAddModel> itemAddModel;
    private User user;

    public InventoryUpdateModel(List<ItemAddModel> itemAddModel, User user) {
        this.itemAddModel = itemAddModel;
        this.user = user;
    }

    public InventoryUpdateModel() {
    }

    public List<ItemAddModel> getItemAddModel() {
        return itemAddModel;
    }

    public void setItemAddModel(List<ItemAddModel> itemAddModel) {
        this.itemAddModel = itemAddModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
