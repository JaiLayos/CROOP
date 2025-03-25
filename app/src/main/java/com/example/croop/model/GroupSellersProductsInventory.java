package com.example.croop.model;

import java.time.LocalDate;

public class GroupSellersProductsInventory extends BaseInventory{
    private int id;
    private GroupSellers groupSellers;
    private GroupSellersDiscount groupSellerDiscounts;
    private int price;
    private int shelfLifeDays;
    private LocalDate localDate;
    public GroupSellersProductsInventory(){

    }

    public GroupSellersProductsInventory(GroupSellers groupSellers, String itemName, int itemStart, int itemUsed,
                                         int itemRemaining, String unit, int shelfLifeDays, LocalDate localDate){
        super(itemName, itemStart, itemUsed,itemRemaining, unit);
        this.groupSellers = groupSellers;
        this.shelfLifeDays = shelfLifeDays;
        this.localDate = localDate;
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

    public int getShelfLifeDays() {
        return shelfLifeDays;
    }
    public void setShelfLifeDays(int shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
