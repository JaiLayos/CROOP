package com.jai.croop.model;

public class DiscountDTO {
    private int discountID;
    private int sellerID;
    private int productID;
    private String sellerName;
    private String sellerRole;
    private String firebaseID;
    private String itemName;
    private int originalPrice;
    private double discountPercent;
    private int salePrice;

    public DiscountDTO(){

    }

    public DiscountDTO(int discountID, int sellerID, int productID, String sellerName,
                       String sellerRole, String firebaseID, String itemName, int originalPrice, double discountPercent, int salePrice){
        this.discountID = discountID;
        this.sellerID = sellerID;
        this.productID = productID;
        this.sellerName = sellerName;
        this.sellerRole = sellerRole;
        this.firebaseID = firebaseID;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
        this.salePrice = salePrice;
    }

    public int getDiscountID() {
        return discountID;
    }
    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    public int getProductID() {
        return productID;
    }
    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getSellerID() {
        return sellerID;
    }
    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getSellerName() {
        return sellerName;
    }
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerRole() {
        return sellerRole;
    }
    public void setSellerRole(String sellerRole) {
        this.sellerRole = sellerRole;
    }

    public String getFirebaseID() {
        return firebaseID;
    }
    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
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

    public double getDiscountPercent() {
        return discountPercent;
    }
    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getSalePrice() {
        return salePrice;
    }
    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }
}
