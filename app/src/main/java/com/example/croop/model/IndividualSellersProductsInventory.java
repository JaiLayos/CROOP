package com.example.croop.model;

public class IndividualSellersProductsInventory extends BaseInventory {
    private int id;
    private IndividualSellers individualSellers;
    private IndividualSellersDiscount individualSellersDiscount;
    private int price;
    public IndividualSellersProductsInventory(){

    }

    public IndividualSellersProductsInventory(IndividualSellers individualSellers, IndividualSellersDiscount individualSellersDiscount, String itemName, int itemStart, int itemUsed,
                                              int itemRemaining, String unit){
        super(itemName, itemStart, itemUsed,itemRemaining, unit);
        this.individualSellers = individualSellers;
        this.individualSellersDiscount = individualSellersDiscount;
    }

    public int getId() {
        return id;
    }

    public IndividualSellers getIndividualSellers() {
        return individualSellers;
    }

    public void setIndividualSellers(IndividualSellers individualSellers) {
        this.individualSellers = individualSellers;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

    public IndividualSellersDiscount getIndividualSellersDiscount() {
        return individualSellersDiscount;
    }
    public void setIndividualSellersDiscount(IndividualSellersDiscount individualSellersDiscount) {
        this.individualSellersDiscount = individualSellersDiscount;
    }
}
