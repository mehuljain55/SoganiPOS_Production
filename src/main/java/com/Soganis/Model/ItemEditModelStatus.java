
package com.Soganis.Model;

import java.util.List;

public class ItemEditModelStatus {
     private List<ItemEditModel> itemEditModelList;
     private String status;

    public List<ItemEditModel> getItemEditModelList() {
        return itemEditModelList;
    }

    public void setItemEditModelList(List<ItemEditModel> itemEditModelList) {
        this.itemEditModelList = itemEditModelList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
     
}
