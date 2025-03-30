package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "customer_orders_individual")
public class CustomerOrdersForIndivSellers extends BaseOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("customer-orders")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "group_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("individual-orders")
    private IndividualSellers individualSellers;


    public CustomerOrdersForIndivSellers() {
        super();
    }

    public CustomerOrdersForIndivSellers(Customer customer, IndividualSellers individualSellers, Map<String, Integer> orderList, int orderPrice, java.util.Date orderDate,
                                         String orderType, String orderStatus, String buyerLocation, String sellerLocation,
                                         String sellerType, String deliveryDetails) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus, buyerLocation, sellerLocation, sellerType, deliveryDetails);
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

    @JsonProperty("customerId")
    public int getCustomerId() {
        return customer.getId();
    }

    @JsonProperty("groupSellerId")
    public int getGroupSellerId() {
        return individualSellers.getId();
    }
}
