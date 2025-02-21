package com.example.croop.model;

public class BaseDiscount {
    private int originalPrice;
    private long discountPercent;
    private int salePrice;

    public BaseDiscount(){

    }

    public BaseDiscount(int originalPrice, int discountPercent, int salePrice){
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
        this.salePrice = salePrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }
    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public long getDiscountPercent() {
        return discountPercent;
    }
    public void setDiscountPercent(long discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getSalePrice() {
        return salePrice;
    }
    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }
}