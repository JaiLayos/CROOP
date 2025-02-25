package com.example.croop.model;

public class IndividualSellersItemInventory extends BaseInventory {
    private int id;
    private IndividualSellers individualSellers;

    public IndividualSellersItemInventory(){

    }

    public IndividualSellersItemInventory(IndividualSellers individualSellers, String itemName, int itemStart, int itemUsed,
                                          int itemRemaining){
        super(itemName, itemStart, itemUsed,itemRemaining);
        this.individualSellers = individualSellers;
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
}
