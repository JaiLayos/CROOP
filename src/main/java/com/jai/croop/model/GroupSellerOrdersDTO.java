package com.jai.croop.model;

import java.util.Date;
import java.util.Map;

public class GroupSellerOrdersDTO {
    private int id;
    private Date orderDate;
    private Map<String, Integer> orderList;
    private int orderPrice;
    private String orderType;
    private String orderStatus;
    private int customerId; // Include the customer ID
    private String customerName; // Optionally include the customer name

    // Constructor, getters, setters...
    public GroupSellerOrdersDTO(int id, Date orderDate, Map<String, Integer> orderList,
                                 int orderPrice, String orderType, String orderStatus, int customerId, String customerName) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderList = orderList;
        this.orderPrice = orderPrice;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public Date getOrderDate(){
        return new Date(orderDate.getTime());
    }

    public void setOrderList(Map<String, Integer> orderList){
        this.orderList = orderList;
    }
    public Map<String, Integer> getOrderList() {
        return orderList;
    }

    public void setOrderPrice(int orderPrice){
        this.orderPrice = orderPrice;
    }
    public int getOrderPrice(){
        return orderPrice;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public String getOrderType() {
        return orderType;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerName() {
        return customerName;
    }
}