package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int cropID;
    private String cropName;
    private int quantity;
    private int price;

    @ManyToOne(optional = true)
    @JoinColumn(name = "group_sellers_id", nullable = true)
    @JsonBackReference("cart-group-seller") // Avoids JSON recursion
    private GroupSellers groupSellers;

    @ManyToOne(optional = true)
    @JoinColumn(name = "individual_sellers_id", nullable = true)
    @JsonBackReference("cart-individual-seller") // Avoids JSON recursion
    private IndividualSellers individualSellers;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference("cart-customer")
    private Customer customer;

    public Cart(){

    }

    public Cart(int id, int cropID, String cropName, int quantity, int price, GroupSellers groupSellers,
    IndividualSellers individualSellers, Customer customer){
        this.id = id;
        this.cropID = cropID;
        this.cropName = cropName;
        this.quantity = quantity;
        this.price = price;
        this.groupSellers = groupSellers;
        this.individualSellers = individualSellers;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCropID() {
        return cropID;
    }
    public void setCropID(int cropID) {
        this.cropID = cropID;
    }

    public String getCropName() {
        return cropName;
    }
    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public GroupSellers getGroupSellers() {
        return groupSellers;
    }
    public void setGroupSellers(GroupSellers groupSellers) {
        this.groupSellers = groupSellers;
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
