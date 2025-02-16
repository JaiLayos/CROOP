package com.jai.croop.model;

public class DiscountDTO {
    private String sellerName;
    private String itemName;
    private int originalPrice;
    private long discountPercent;
    private int salePrice;

    public DiscountDTO(){

    }

    public DiscountDTO(String sellerName, String itemName, int originalPrice, int discountPercent, int salePrice){
        this.sellerName = sellerName;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
        this.salePrice = salePrice;
    }

    public String getSellerName() {
        return sellerName;
    }
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
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
