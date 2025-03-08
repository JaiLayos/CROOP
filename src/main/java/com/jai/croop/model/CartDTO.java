package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class CartDTO {
    private int id;
    private String cropName;
    private int quantity;
    private int price;
    private int sellerID;
    private String sellerName;
    private int customerID;
    private String customerName;

    public CartDTO(){

    }

    public CartDTO(int id, String cropName, int quantity, int price, int sellerID, String sellerName,
                   int customerID, String customerName){
        this.id = id;
        this.cropName = cropName;
        this.quantity = quantity;
        this.price = price;
        this.sellerID = sellerID;
        this.sellerName = sellerName;
        this.customerID = customerID;
        this.customerName = customerName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCropName() {
        return cropName;
    }
    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
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

    public int getCustomerID() {
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
