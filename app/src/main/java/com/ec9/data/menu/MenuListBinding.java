package com.ec9.data.menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/18/2014.
 */
public class MenuListBinding {
    private int MenuTagID;
    private int CustomerID;
    private String MenuTagName;
    private List<MenuBinding> Menus;

    public int getMenuTagID() {
        return MenuTagID;
    }

    public void setMenuTagID(int menuTagID) {
        MenuTagID = menuTagID;
    }

    public String getMenuTagName() {
        return MenuTagName;
    }

    public void setMenuTagName(String menuTagName) {
        MenuTagName = menuTagName;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public List<MenuBinding> getMenus() {
        return Menus;
    }

    public void setMenus(List<MenuBinding> menus) {
        Menus = menus;
    }
}


