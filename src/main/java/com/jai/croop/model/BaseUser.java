package com.jai.croop.model;

import com.jai.croop.AddressConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;

import java.util.Date;
import java.util.Map;

@MappedSuperclass
public class BaseUser {
    private String firebaseID;
    private String name;
    @Convert(converter = AddressConverter.class)
    private Map<String, String> address;
    private String phoneNumber;
    private String email;
    private String bio;
    private Date createdAt;
    private Date updatedAt;
    private String roles;
    private int product_inventory_SC; //setup cost
    private int product_inventory_MC; //managing cost

    public BaseUser(){

    }

    public BaseUser(String firebaseID, String name, Map<String, String> address, String phoneNumber,
                    String email, String bio, Date createdAt, Date updatedAt, String roles,
                    int product_inventory_SC, int product_inventory_MC) {
        this.firebaseID = firebaseID;
        this.name =  name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roles = roles;
        this.product_inventory_SC = product_inventory_SC;
        this.product_inventory_MC = product_inventory_MC;
    }

    public void setFirebaseID(String firebaseID){this.firebaseID= firebaseID;}
    public String getFirebaseID() {
        return firebaseID;
    }

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAddress(){
        return address;
    }
    public void setAddress(Map<String, String> address) {
        this.address = address;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio(){
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRoles(){
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getProduct_inventory_MC() {
        return product_inventory_MC;
    }
    public void setProduct_inventory_MC(int product_inventory_MC) {
        this.product_inventory_MC = product_inventory_MC;
    }

    public int getProduct_inventory_SC() {
        return product_inventory_SC;
    }
    public void setProduct_inventory_SC(int product_inventory_SC) {
        this.product_inventory_SC = product_inventory_SC;
    }
}
