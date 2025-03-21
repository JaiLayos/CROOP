package com.example.croop.model;

public class OrderItem {
    private String cropName;
    private int quantity;

    public OrderItem(){

    }

    public OrderItem(String cropName, int quantity){
        this.cropName = cropName;
        this.quantity = quantity;
    }
    // Getters and setters
    public String getItem() {
        return cropName;
    }
    public void setItem(String cropName) {
        this.cropName = cropName;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
