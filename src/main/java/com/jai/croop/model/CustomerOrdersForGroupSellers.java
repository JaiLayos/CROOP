package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "customer_orders_group")
public class CustomerOrdersForGroupSellers extends BaseOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("customer-orders")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "group_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("group-orders")
    private GroupSellers groupSeller;


    public CustomerOrdersForGroupSellers() {
        super();
    }

    public CustomerOrdersForGroupSellers(Customer customer, GroupSellers groupSeller, Map<String, Integer> orderList, int orderPrice, java.util.Date orderDate,
                                         String orderType, String orderStatus, String buyerLocation, String sellerLocation) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus, buyerLocation, sellerLocation);
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

    @JsonProperty("customerId")
    public int getCustomerId() {
        return customer.getId();
    }

    @JsonProperty("groupSellerId")
    public int getGroupSellerId() {
        return groupSeller.getId();
    }

}
