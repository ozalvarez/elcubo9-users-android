package com.ec9.data.order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/19/2014.
 */
public class OrderBinding {
    private int OrderID;
    private String TableNumber;
    private int CustomerID;
    private String CustomerName;
    private List<OrderItemBinding> Items;

    public OrderBinding(){
        Items=new ArrayList<OrderItemBinding>();
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getTableNumber() {
        return TableNumber;
    }

    public void setTableNumber(String tableNumber) {
        TableNumber = tableNumber;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public List<OrderItemBinding> getItems() {
        return Items;
    }

    public void setItems(List<OrderItemBinding> items) {
        Items = items;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }
}
