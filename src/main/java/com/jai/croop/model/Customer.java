package com.jai.croop.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name="individual_customers")
public class Customer extends BaseUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer-orders")
    private List<CustomerOrdersForGroupSellers> customerOrders;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer-group-orders")
    private List<GroupSellersOrders> groupSellerOrders;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer-individual-orders")
    private List<IndividualSellersOrders> individualSellersOrders;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("cart-customer") // Matches @JsonBackReference in Cart
    private List<Cart> carts;

    public Customer() {}

    public Customer(String firebaseID, String name, Map<String, String> address, String phoneNumber,
                    String email, String bio, Date createdAt, Date updatedAt, String roles) {
        super(firebaseID, name, address, phoneNumber, email, bio, createdAt, updatedAt, roles);
    }

    public int getId() {
        return id;
    }

    public List<CustomerOrdersForGroupSellers> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrdersForGroupSellers> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<GroupSellersOrders> getGroupSellerOrders() {
        return groupSellerOrders;
    }

    public void setGroupSellerOrders(List<GroupSellersOrders> groupSellerOrders) {
        this.groupSellerOrders = groupSellerOrders;
    }

    public List<IndividualSellersOrders> getIndividualSellersOrders() {
        return individualSellersOrders;
    }
    public void setIndividualSellersOrders(List<IndividualSellersOrders> individualSellersOrders) {
        this.individualSellersOrders = individualSellersOrders;
    }

    public List<Cart> getCarts() {
        return carts;
    }
    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}

