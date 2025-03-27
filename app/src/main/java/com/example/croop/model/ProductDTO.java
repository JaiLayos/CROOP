package com.example.croop.model;

public class ProductDTO {
    private int productID;
    private String productName;
    private int productPrice;
    private int sellerID;
    private String productSeller;
    private String sellerRole;
    private String firebaseID;
    private String unit;
    private double productDiscount;
    private int productFinalPrice;
    private int remaining;

    public ProductDTO(){

    }

    public ProductDTO(int productID, String productName, String firebaseID, int sellerID,
                      int productPrice, String productSeller, String sellerRole,
                      double productDiscount, int productFinalPrice,
                      int remaining, String unit){
        this.productID = productID;
        this.productName = productName;
        this.firebaseID = firebaseID;
        this.sellerID = sellerID;
        this.productPrice = productPrice;
        this.productSeller = productSeller;
        this.sellerRole = sellerRole;
        this.productDiscount = productDiscount;
        this.productFinalPrice = productFinalPrice;
        this.remaining = remaining;
        this.unit = unit;
    }

    public int getProductID() {
        return productID;
    }
    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFirebaseID() {
        return firebaseID;
    }
    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public int getSellerID() {
        return sellerID;
    }
    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getProductSeller() {
        return productSeller;
    }
    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public String getSellerRole() {
        return sellerRole;
    }
    public void setSellerRole(String sellerRole) {
        this.sellerRole = sellerRole;
    }

    public int getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductDiscount() {
        return productDiscount;
    }
    public void setProductDiscount(double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public int getProductFinalPrice() {
        return productFinalPrice;
    }
    public void setProductFinalPrice(int productFinalPrice) {
        this.productFinalPrice = productFinalPrice;
    }

    public int getRemaining() {
        return remaining;
    }
    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}