package com.jai.croop.model;

import java.util.List;

public class GroupSellerCartDTO {
    private int id;
    private String groupName;
    private List<CartDTO> cartItems;
    public GroupSellerCartDTO(){

    }

    public GroupSellerCartDTO(int id, String groupName, List<CartDTO> cartItems){
        this.id = id;
        this.groupName = groupName;
        this.cartItems = cartItems;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<CartDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
