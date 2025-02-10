package com.jai.croop.model;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "customer_orders")
public class CustomerOrders extends BaseOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "group_seller_id", referencedColumnName = "id", nullable = false)
    private GroupSellers groupSeller;

    public CustomerOrders() {
        super();
    }

    public CustomerOrders(Customer customer, GroupSellers groupSeller, Map<String, Integer> orderList,  int orderPrice, java.util.Date orderDate,
                          String orderType, String orderStatus) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus);
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
}
