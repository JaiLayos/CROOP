package com.example.croop.model;

public class IndividualSellersDiscount extends BaseDiscount {
    private int id;
    private IndividualSellers individualSellers;
    private IndividualSellersProductsInventory individualSellersProductsInventory;

    private String productName;
    public IndividualSellersDiscount(){

    }

    public IndividualSellersDiscount(int id, IndividualSellers individualSellers, IndividualSellersProductsInventory individualSellersProductsInventory, int originalPrice, int discountPercent, int salePrice){
        super(originalPrice, discountPercent, salePrice);
        this.id = id;
        this.individualSellers = individualSellers;
        this.individualSellersProductsInventory = individualSellersProductsInventory;
    }

    public int getId() {
        return id;
    }

    public void setIndividualSellers(IndividualSellers individualSellers) {
        this.individualSellers = individualSellers;
    }
    public IndividualSellers getIndividualSellers() {
        return individualSellers;
    }

    public IndividualSellersProductsInventory getIndividualSellersProductsInventory() {
        return individualSellersProductsInventory;
    }
    public void setIndividualSellersProductsInventory(IndividualSellersProductsInventory individualSellersProductsInventory) {
        this.individualSellersProductsInventory = individualSellersProductsInventory;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
