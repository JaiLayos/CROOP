package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "group_seller_orders")
public class GroupSellersOrders extends BaseOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "group_seller_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private GroupSellers groupSeller;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Customer customer;

    public GroupSellersOrders() {

    }

    public GroupSellersOrders(GroupSellers groupSeller, Customer customer,
                              Map<String, Integer> orderList,
                              int orderPrice, Date orderDate, String orderType, String orderStatus) {
        super(orderList, orderPrice, orderDate, orderType, orderStatus);
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
