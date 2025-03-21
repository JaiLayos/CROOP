package com.example.croop.model;

import java.util.List;

public class IndividualSellerCartDTO {
    private int id;
    private String name;
    private List<CartDTO> cartItems;

    public IndividualSellerCartDTO(){

    }

    public IndividualSellerCartDTO(int id, String name, List<CartDTO> cartItems) {
        this.id = id;
        this.name = name;
        this.cartItems = cartItems;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<CartDTO> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<CartDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
