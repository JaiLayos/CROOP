package com.jai.croop.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jai.croop.AddressConverter;
import jakarta.persistence.*;

@Entity
@Table(name="individual_customers")
public class Customer extends BaseUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer-orders")
    private List<CustomerOrders> customerOrders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer-group-orders")
    private List<GroupSellersOrders> groupSellerOrders;

    public Customer() {}

    public Customer(String firebaseID, String name, Map<String, String> address, String phoneNumber,
                    String email, String bio, Date createdAt, Date updatedAt, String roles) {
        super(firebaseID, name, address, phoneNumber, email, bio, createdAt, updatedAt, roles);
    }

    public int getId() {
        return id;
    }

    public List<CustomerOrders> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrders> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<GroupSellersOrders> getGroupSellerOrders() {
        return groupSellerOrders;
    }

    public void setGroupSellerOrders(List<GroupSellersOrders> groupSellerOrders) {
        this.groupSellerOrders = groupSellerOrders;
    }
}

