package com.Soganis.Model;

import java.util.List;

public class ItemApiResponseModel {
    private List<ItemModel> itemModelList;
    private String status;

    public ItemApiResponseModel(List<ItemModel> itemModelList, String status) {
        this.itemModelList = itemModelList;
        this.status = status;
    }

    public ItemApiResponseModel() {
    }

    public List<ItemModel> getItemModelList() {
        return itemModelList;
    }

    public void setItemModelList(List<ItemModel> itemModelList) {
        this.itemModelList = itemModelList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
