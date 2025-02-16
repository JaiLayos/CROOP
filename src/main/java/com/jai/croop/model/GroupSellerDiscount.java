package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "group_sellers_discounts")
public class GroupSellerDiscount extends BaseDiscount{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "group_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("group-discounts")
    private GroupSellers groupSellers;

    @OneToOne
    @JoinColumn(name = "group_products_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("group-products-discounted")
    private GroupSellersProductsInventory groupSellersProductsInventory;

    private String productName;
    public GroupSellerDiscount(){

    }

    public GroupSellerDiscount(GroupSellers groupSellers, GroupSellersProductsInventory groupSellersProductsInventory, int originalPrice, int discountPercent, int salePrice){
        super(originalPrice, discountPercent, salePrice);
        this.groupSellers = groupSellers;
        this.groupSellersProductsInventory = groupSellersProductsInventory;
    }

    public void setGroupSellers(GroupSellers groupSellers) {
        this.groupSellers = groupSellers;
    }
    public GroupSellers getGroupSellers() {
        return groupSellers;
    }

    public GroupSellersProductsInventory getGroupSellersProductsInventory() {
        return groupSellersProductsInventory;
    }
    public void setGroupSellersProductsInventory(GroupSellersProductsInventory groupSellersProductsInventory) {
        this.groupSellersProductsInventory = groupSellersProductsInventory;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
