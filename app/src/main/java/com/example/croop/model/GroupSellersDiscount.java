package com.example.croop.model;

public class GroupSellersDiscount extends BaseDiscount{

    private int id;
    private GroupSellers groupSellers;

    private GroupSellersProductsInventory groupSellersProductsInventory;

    private String productName;
    public GroupSellersDiscount(){

    }

    public GroupSellersDiscount(GroupSellers groupSellers, GroupSellersProductsInventory groupSellersProductsInventory, int originalPrice, int discountPercent, int salePrice){
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
