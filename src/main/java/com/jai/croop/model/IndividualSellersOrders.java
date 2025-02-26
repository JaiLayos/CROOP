package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "individual_sellers_orders")
public class IndividualSellersOrders extends BaseOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "individual_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("individual-orders")
    private IndividualSellers individualSellers;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("customer-individual-orders")
    private Customer customer;

    public IndividualSellersOrders() {

    }

    public IndividualSellersOrders(IndividualSellers individualSellers, Customer customer,
                              Map<String, Integer> orderList,
                              int orderPrice, Date orderDate, String orderType, String orderStatus) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus);
        this.individualSellers = individualSellers;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public IndividualSellers getIndividualSellers() {
        return individualSellers;
    }

    public void setIndividualSellers(IndividualSellers individualSellers) {
        this.individualSellers = individualSellers;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
