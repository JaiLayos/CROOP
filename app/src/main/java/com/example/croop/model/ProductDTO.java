package com.example.croop.model;

public class ProductDTO {
    private int productID;
    private String productName;
    private int productPrice;
    private String productSeller;
    private String productSellerType;
    private String firebaseID;
    private double productDiscount;
    private int productFinalPrice;

    public ProductDTO(){

    }

    public ProductDTO(int productID, String productName, int productPrice, String productSeller,
                      String productSellerType, String firebaseID, double productDiscount, int productFinalPrice){
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSeller = productSeller;
        this.productSellerType = productSellerType;
        this.firebaseID = firebaseID;
        this.productDiscount = productDiscount;
        this.productFinalPrice = productFinalPrice;
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

    public String getProductSeller() {
        return productSeller;
    }
    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public String getProductSellerType() {
        return productSellerType;
    }
    public void setProductSellerType(String productSellerType) {
        this.productSellerType = productSellerType;
    }

    public String getFirebaseID() {
        return firebaseID;
    }
    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
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
}
