package com.example.croop.model;

import java.util.Map;

public class CustomerOrdersForIndivSellers extends BaseOrder{
    private int id;
    private Customer customer;
    private IndividualSellers individualSellers;


    public CustomerOrdersForIndivSellers() {
        super();
    }

    public CustomerOrdersForIndivSellers(Customer customer, IndividualSellers individualSellers, Map<String, Integer> orderList, int orderPrice, java.util.Date orderDate,
                                         String orderType, String orderStatus) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus);
        this.customer = customer;
        this.individualSellers = individualSellers;
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

    public IndividualSellers getIndividualSellers() {
        return individualSellers;
    }

    public void setIndividualSellers(IndividualSellers individualSellers) {
        this.individualSellers = individualSellers;
    }

    public int getCustomerId() {
        return customer.getId();
    }

    public int getGroupSellerId() {
        return individualSellers.getId();
    }



}
