package com.example.croop.model;

public class Cart {

    private int id;
    private int cropID;
    private String cropName;
    private int quantity;
    private int price;
    private GroupSellers groupSellers;
    private IndividualSellers individualSellers;
    private Customer customer;

    public Cart(){

    }

    public Cart(int id, int cropID, String cropName, int quantity, int price, GroupSellers groupSellers,
                IndividualSellers individualSellers, Customer customer){
        this.id = id;
        this.cropID = cropID;
        this.cropName = cropName;
        this.quantity = quantity;
        this.price = price;
        this.groupSellers = groupSellers;
        this.individualSellers = individualSellers;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCropID() {
        return cropID;
    }
    public void setCropID(int cropID) {
        this.cropID = cropID;
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

    public GroupSellers getGroupSellers() {
        return groupSellers;
    }
    public void setGroupSellers(GroupSellers groupSellers) {
        this.groupSellers = groupSellers;
    }

    public IndividualSellers getIndividualSellers() {
        return individualSellers;
    }
    public void setIndividualSellers(IndividualSellers individualSellers) {
        this.individualSellers = individualSellers;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
