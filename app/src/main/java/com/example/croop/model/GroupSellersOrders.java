package com.example.croop.model;

import java.util.Date;
import java.util.Map;

public class GroupSellersOrders extends BaseOrder{

    private int id;
    private GroupSellers groupSeller;

    private Customer customer;

    public GroupSellersOrders() {

    }

    public GroupSellersOrders(GroupSellers groupSeller, Customer customer,
                              Map<String, Integer> orderList,
                              int orderPrice, Date orderDate, String orderType, String orderStatus,
                              String buyerLocation, String sellerLocation) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus, buyerLocation, sellerLocation);
        this.groupSeller = groupSeller;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public GroupSellers getGroupSeller() {
        return groupSeller;
    }

    public void setGroupSeller(GroupSellers groupSeller) {
        this.groupSeller = groupSeller;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
