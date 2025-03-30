package com.example.croop.model;


import java.util.Map;

public class CustomerOrdersForGroupSellers extends BaseOrder{
    private int id;
    private Customer customer;
    private GroupSellers groupSeller;


    public CustomerOrdersForGroupSellers() {
        super();
    }

    public CustomerOrdersForGroupSellers(Customer customer, GroupSellers groupSeller, Map<String, Integer> orderList, int orderPrice, java.util.Date orderDate,
                                         String orderType, String orderStatus, String buyerLocation, String sellerLocation,
                                         String sellerType, String deliveryDetails) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus, buyerLocation, sellerLocation, sellerType, deliveryDetails);
        this.customer = customer;
        this.groupSeller = groupSeller;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public GroupSellers getGroupSeller() {
        return groupSeller;
    }

    public void setGroupSeller(GroupSellers groupSeller) {
        this.groupSeller = groupSeller;
    }


    public int getCustomerId() {
        return customer.getId();
    }

    public int getGroupSellerId() {
        return groupSeller.getId();
    }

}

