package com.ec9.data.order;

import com.ec9.data.menu.AdditionalItemBinding;

/**
 * Created by Oswaldo on 11/20/2014.
 */
public class OrderItemAdditionalBinding {
    private Double Price;
    private String AdditionalItemName;
    private int AdditionalItemID;

    public OrderItemAdditionalBinding(AdditionalItemBinding add){
        this.AdditionalItemID=add.getAdditionalItemID();
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getAdditionalItemName() {
        return AdditionalItemName;
    }

    public void setAdditionalItemName(String additionalItemName) {
        AdditionalItemName = additionalItemName;
    }

    public int getAdditionalItemID() {
        return AdditionalItemID;
    }

    public void setAdditionalItemID(int additionalItemID) {
        AdditionalItemID = additionalItemID;
    }
}
