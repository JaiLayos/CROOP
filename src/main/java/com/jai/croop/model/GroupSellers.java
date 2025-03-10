package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "group_business_users")
public class GroupSellers extends BaseUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String groupName;
    private String position;

    @OneToMany(mappedBy = "groupSeller", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("group-inventory")
    private List<GroupSellersItemInventory> groupSellersItemInventoryList;

    @OneToMany(mappedBy = "groupSellers", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("group-products")
    private List<GroupSellersProductsInventory> groupSellersProductsInventoryList;

    @OneToMany(mappedBy = "groupSellers", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("group-discounts")
    private List<GroupSellerDiscount> groupSellerDiscounts;
    @OneToMany(mappedBy = "groupSellers", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("cart-group-seller")
    private List<Cart> carts;

    public GroupSellers() {}

    public GroupSellers(String firebaseID, String name, Map<String, String> address, String phoneNumber,
                        String email, String bio, Date createdAt, Date updatedAt, String roles,
                        String groupName, String position) {
        super(firebaseID, name, address, phoneNumber, email, bio, createdAt, updatedAt, roles);
        this.groupName = groupName;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<GroupSellersItemInventory> getGroupSellerItems() {
        return groupSellersItemInventoryList;
    }

    public void setGroupSellerItem(List<GroupSellersItemInventory> groupSellersItemInventoryList) {
        this.groupSellersItemInventoryList = groupSellersItemInventoryList;
    }

    public List<GroupSellersProductsInventory> getGroupSellersProductsInventoryList() {
        return groupSellersProductsInventoryList;
    }
    public void setGroupSellersProductsInventoryList(List<GroupSellersProductsInventory> groupSellersProductsInventoryList) {
        this.groupSellersProductsInventoryList = groupSellersProductsInventoryList;
    }

    public List<Cart> getCarts() {
        return carts;
    }
    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
