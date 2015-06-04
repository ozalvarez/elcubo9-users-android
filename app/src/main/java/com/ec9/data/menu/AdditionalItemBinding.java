package com.ec9.data.menu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Oswaldo on 11/19/2014.
 */
public class AdditionalItemBinding {
    private int AdditionalItemID;
    private String AdditionalItemName;
    private int AdditionalID;
    private Double Price;
    private Boolean Active;
    public AdditionalItemBinding(){
        Active=false;
    }

    public int getAdditionalItemID() {
        return AdditionalItemID;
    }

    public String getAdditionalItemName() {
        return AdditionalItemName;
    }

    public int getAdditionalID() {
        return AdditionalID;
    }

    public Double getPrice() {
        return Price;
    }

    public void setAdditionalItemID(int additionalItemID) {
        AdditionalItemID = additionalItemID;
    }

    public void setAdditionalItemName(String additionalItemName) {
        AdditionalItemName = additionalItemName;
    }

    public void setAdditionalID(int additionalID) {
        AdditionalID = additionalID;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }
}
