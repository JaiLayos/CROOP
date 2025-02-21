package com.example.croop.model;

public class GroupSellersProductsInventory extends BaseInventory{
    private int id;
    private GroupSellers groupSellers;
    private GroupSellersDiscount groupSellerDiscounts;
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

    public GroupSellersDiscount getGroupSellerDiscounts() {
        return groupSellerDiscounts;
    }
    public void setGroupSellerDiscounts(GroupSellersDiscount groupSellerDiscounts) {
        this.groupSellerDiscounts = groupSellerDiscounts;
    }
}
