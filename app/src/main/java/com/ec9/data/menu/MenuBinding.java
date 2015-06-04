package com.ec9.data.menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/18/2014.
 */
public class MenuBinding {
    private int CustomerID;
    private String Tag;
    private int MenuID;
    private String Title;
    private String Subtitle;
    private int MenuTagID;
    private double Price;
    private List<MenuAdditionalBinding> Items;

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public int getMenuID() {
        return MenuID;
    }

    public void setMenuID(int menuID) {
        MenuID = menuID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public int getMenuTagID() {
        return MenuTagID;
    }

    public void setMenuTagID(int menuTagID) {
        MenuTagID = menuTagID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public List<MenuAdditionalBinding> getItems() {
        return Items;
    }

    public void setItems(List<MenuAdditionalBinding> items) {
        Items = items;
    }



}
