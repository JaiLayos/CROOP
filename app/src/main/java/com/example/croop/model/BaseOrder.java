package com.example.croop.model;

import java.util.Date;
import java.util.Map;

public class BaseOrder {
    private Map<String, Integer> orderList;
    private int orderPrice;
    private final Date orderDate;
    private String orderType;
    private String orderStatus;
    private String buyerLocation;
    private String sellerLocation;

    public BaseOrder(){
        this.orderDate = new Date();
    }
    public BaseOrder(Map<String, Integer> orderList, int orderPrice, Date orderDate,
                     String orderType,String orderStatus, String buyerLocation, String sellerLocation){
        this.orderList = orderList;
        this.orderPrice = orderPrice;
        this.orderDate = (orderDate != null) ? new Date(orderDate.getTime()) : new Date();
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.buyerLocation = buyerLocation;
        this.sellerLocation = sellerLocation;
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

    public Date getOrderDate(){
        return new Date(orderDate.getTime());
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

    public void setBuyerLocation(String buyerLocation) {
        this.buyerLocation = buyerLocation;
    }
    public String getBuyerLocation() {
        return buyerLocation;
    }

    public void setSellerLocation(String sellerLocation) {
        this.sellerLocation = sellerLocation;
    }
    public String getSellerLocation() {
        return sellerLocation;
    }
}
