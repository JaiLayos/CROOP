package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "individual_sellers")
public class IndividualSellers extends BaseUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "individualSellers", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("individual-orders")
    private List<IndividualSellersOrders> individualSellersOrders;

    @OneToMany(mappedBy = "individualSellers", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("individual-inventory")
    private List<IndividualSellersItemInventory> individualSellersItemInventory;

    @OneToMany(mappedBy = "individualSellers", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("individual-products")
    private List<IndividualSellersProductsInventory> individualSellersProductsInventory;

    @OneToMany(mappedBy = "individualSellers", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("individual-discounts")
    private List<IndividualSellersDiscount> individualSellersDiscount;
    @OneToMany(mappedBy = "individualSellers", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("cart-individual-seller")
    private List<Cart> carts;

    public IndividualSellers() {}

    public IndividualSellers(String firebaseID, String name, Map<String, String> address, String phoneNumber,
                        String email, String bio, Date createdAt, Date updatedAt, String roles) {
        super(firebaseID, name, address, phoneNumber, email, bio, createdAt, updatedAt, roles);
    }

    public int getId() {
        return id;
    }

    public List<IndividualSellersOrders> getIndividualSellerOrders() {
        return individualSellersOrders;
    }

    public void setIndividualSellerOrders(List<IndividualSellersOrders> individualSellersOrders) {
        this.individualSellersOrders = individualSellersOrders;
    }

    public List<IndividualSellersItemInventory> getIndividualSellerItems() {
        return individualSellersItemInventory;
    }

    public void setIndividualSellerItem(List<IndividualSellersItemInventory> individualSellersItemInventory) {
        this.individualSellersItemInventory = individualSellersItemInventory;
    }
    public List<IndividualSellersProductsInventory> getIndividualSellerProducts() {
        return individualSellersProductsInventory;
    }

    public void setIndividualSellersProducts(List<IndividualSellersProductsInventory> individualSellersProductsInventory) {
        this.individualSellersProductsInventory = individualSellersProductsInventory;
    }

    public List<IndividualSellersDiscount> getIndividualSellersDiscount() {
        return individualSellersDiscount;
    }

    public void setIndividualSellersDiscount(List<IndividualSellersDiscount> individualSellersDiscount) {
        this.individualSellersDiscount = individualSellersDiscount;
    }

    public List<Cart> getCarts() {
        return carts;
    }
    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
