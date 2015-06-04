package com.ec9.data.order;

import com.ec9.data.menu.AdditionalItemBinding;
import com.ec9.data.menu.MenuAdditionalBinding;
import com.ec9.data.menu.MenuBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/19/2014.
 */
public class OrderItemBinding {
    private String Title;
    private String Subtitle;
    private Double Price;
    private int MenuID;
    private int Quantity;
    private String AdditionalInfo;
    private List<OrderItemAdditionalBinding> Items;

    public OrderItemBinding(MenuBinding menuItemToAdd,int quantity, String additionalInfo){
        this.MenuID=menuItemToAdd.getMenuID();
        this.Quantity=quantity;
        this.AdditionalInfo=additionalInfo;
        this.Title=menuItemToAdd.getTitle();
        this.Subtitle=menuItemToAdd.getSubtitle();
        this.Price=menuItemToAdd.getPrice();
        this.Items=new ArrayList<OrderItemAdditionalBinding>();

        for (int i=0;i<menuItemToAdd.getItems().size();i++){
            MenuAdditionalBinding mAdd=menuItemToAdd.getItems().get(i);
            for (int i2=0;i2<mAdd.getItems().size();i2++){
                AdditionalItemBinding add=mAdd.getItems().get(i2);
                if(add.getActive()) {
                    this.Items.add(new OrderItemAdditionalBinding(add));
                }
            }
        }
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

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public int getMenuID() {
        return MenuID;
    }

    public void setMenuID(int menuID) {
        MenuID = menuID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getAdditionalInfo() {
        return AdditionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        AdditionalInfo = additionalInfo;
    }

    public List<OrderItemAdditionalBinding> getItems() {
        return Items;
    }

    public void setItems(List<OrderItemAdditionalBinding> items) {
        Items = items;
    }
}
