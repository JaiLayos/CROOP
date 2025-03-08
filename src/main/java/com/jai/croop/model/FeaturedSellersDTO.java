package com.jai.croop.model;

public class FeaturedSellersDTO {
    private int id;
    private String name;
    private String role;
    private String firebaseID;

    public FeaturedSellersDTO(){

    }
    public FeaturedSellersDTO(int id, String name, String role, String firebaseID){
        this.id = id;
        this.name = name;
        this.role = role;
        this.firebaseID = firebaseID;
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

    public String getFirebaseID() {
        return firebaseID;
    }
    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }
}
