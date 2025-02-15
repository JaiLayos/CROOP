package com.jai.croop.model;

import com.jai.croop.AddressConverter;
import jakarta.persistence.Convert;

import java.util.Date;
import java.util.Map;

public class GroupSellersOrderDTO {
    private int id;
    private int groupSellerID;
    private int customerID;
    @Convert(converter = AddressConverter.class)
    private Map<String, Integer> orderList;
    private int orderPrice;
    private final Date orderDate = new Date();
    private String orderType;
    private String orderStatus;

    public GroupSellersOrderDTO(){

    }

    public GroupSellersOrderDTO(int id, int groupSellerID, int customerID, Map<String, Integer> orderList, int orderPrice, Date orderDate, String orderType, String orderStatus) {
        this.id = id;
        this.groupSellerID = groupSellerID;
        this.customerID = customerID;
        this.orderList = orderList;
        this.orderPrice = orderPrice;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getGroupSellerID() {
        return groupSellerID;
    }
    public void setGroupSellerID(int groupSellerID) {
        this.groupSellerID = groupSellerID;
    }

    public int getCustomerID() {
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Map<String, Integer> getOrderList() {
        return orderList;
    }
    public void setOrderList(Map<String, Integer> orderList) {
        this.orderList = orderList;
    }

    public int getOrderPrice() {
        return orderPrice;
    }
    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderType() {
        return orderType;
    }
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
