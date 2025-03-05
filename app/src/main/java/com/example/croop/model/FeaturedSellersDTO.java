package com.example.croop.model;

public class FeaturedSellersDTO {
    private int id;
    private String name;
    private String role;

    public FeaturedSellersDTO(){

    }
    public FeaturedSellersDTO(int id, String name, String role){
        this.id = id;
        this.name = name;
        this.role = role;
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

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
