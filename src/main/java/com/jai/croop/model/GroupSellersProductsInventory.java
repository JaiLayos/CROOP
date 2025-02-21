package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "group_seller_products")
public class GroupSellersProductsInventory extends BaseInventory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "group_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("group-products")
    private GroupSellers groupSellers;

    @ManyToOne
    @JsonManagedReference("group-products-discounted")
    private GroupSellerDiscount groupSellerDiscounts;


    private int price;
    public GroupSellersProductsInventory(){

    }

    public GroupSellersProductsInventory(GroupSellers groupSellers, String itemName, int itemStart, int itemUsed,
                                     int itemRemaining){
        super(itemName, itemStart, itemUsed,itemRemaining);
        this.groupSellers = groupSellers;
    }

    public int getId() {
        return id;
    }

    public GroupSellers getGroupSellers() {
        return groupSellers;
    }

    public void setGroupSellers(GroupSellers groupSellers) {
        this.groupSellers = groupSellers;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

    public GroupSellerDiscount getGroupSellerDiscounts() {
        return groupSellerDiscounts;
    }
    public void setGroupSellerDiscounts(GroupSellerDiscount groupSellerDiscounts) {
        this.groupSellerDiscounts = groupSellerDiscounts;
    }
}
