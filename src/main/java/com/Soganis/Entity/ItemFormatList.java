package com.Soganis.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="item_group_list_format")
public class ItemFormatList {
    @Id
    private int sno;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    private String item6;

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getItem5() {
        return item5;
    }

    public void setItem5(String item5) {
        this.item5 = item5;
    }

    public String getItem6() {
        return item6;
    }

    public void setItem6(String item6) {
        this.item6 = item6;
    }
}
