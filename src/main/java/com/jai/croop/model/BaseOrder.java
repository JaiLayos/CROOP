package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jai.croop.AddressConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;

import java.util.Date;
import java.util.Map;

@MappedSuperclass
public class BaseOrder {
    @Convert(converter = AddressConverter.class)
    private Map<String, Integer> orderList;
    private int orderPrice;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date orderDate;
    private String orderType;
    private String orderStatus;
    private String location;

    public BaseOrder(){
        this.orderDate = new Date();
    }
    public BaseOrder(Map<String, Integer> orderList, int orderPrice, Date orderDate,
                     String orderType,String orderStatus, String location){
        this.orderList = orderList;
        this.orderPrice = orderPrice;
        this.orderDate = (orderDate != null) ? new Date(orderDate.getTime()) : new Date();
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.location = location;
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

}
