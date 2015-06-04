package com.ec9.data.menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/18/2014.
 */
public class MenuAdditionalBinding {
    private int MenuAdditionalID;
    private int MenuID;
    private int AdditionalID;
    private String MenuAdditionalName;
    private Boolean Required;
    private List<AdditionalItemBinding> Items;

    public int getMenuAdditionalID() {
        return MenuAdditionalID;
    }

    public void setMenuAdditionalID(int menuAdditionalID) {
        MenuAdditionalID = menuAdditionalID;
    }

    public int getMenuID() {
        return MenuID;
    }

    public void setMenuID(int menuID) {
        MenuID = menuID;
    }

    public int getAdditionalID() {
        return AdditionalID;
    }

    public void setAdditionalID(int additionalID) {
        AdditionalID = additionalID;
    }

    public String getMenuAdditionalName() {
        return MenuAdditionalName;
    }

    public void setMenuAdditionalName(String menuAdditionalName) {
        MenuAdditionalName = menuAdditionalName;
    }

    public Boolean getRequired() {
        return Required;
    }

    public void setRequired(Boolean required) {
        Required = required;
    }

    public List<AdditionalItemBinding> getItems() {
        return Items;
    }


    public CharSequence[] getAdditionals() {
        CharSequence[] list = new CharSequence[Items.size()];
        for (int i = 0; i < Items.size(); i++) {
            list[i] = Items.get(i).getAdditionalItemName();
        }
        return list;
    }
    public boolean[] getChecked() {
        boolean[] list = new boolean[Items.size()];
        for (int i = 0; i < Items.size(); i++) {
            if(Items.get(i).getActive())
                list[i]=true;
            else list[i]=false;
        }
        return list;
    }
}
