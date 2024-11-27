package com.example.croop.model;

import java.util.Date;
import java.util.Map;

public class Customer {
    private int id;
    private String name;
    private Map<String, String> address;
    private String phoneNumber;
    private String email;
    private String bio;
    private Date createdAt;
    private Date updatedAt;
    private String roles;

    public Customer(){

    }

    public Customer(int id, String name, Map<String, String> address, String phoneNumber,
     String email, String bio, Date createdAt, Date updatedAt, String roles){
        this.id = id;
        this.name =  name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roles = roles;
    }
    public int getCust_Id() {
        return id;
    }

    public String getCust_Name(){
        return name;
    }
    public void setCust_Name(String name) {
        this.name = name;
    }

    public Map<String, String> getCust_Address(){
        return address;
    }
    public void setCust_Address(Map<String, String> address) {
        this.address = address;
    }

    public String getCust_PhoneNum(){
        return phoneNumber;
    }
    public void setCust_PhoneNum(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCust_Email(){
        return email;
    }
    public void setCust_Email(String email) {
        this.email = email;
    }

    public String getCust_Bio(){
        return bio;
    }
    public void setCust_Bio(String bio) {
        this.bio = bio;
    }

    public Date getCust_CreatedAt() {
        return createdAt;
    }
    public void setCust_CreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCust_UpdatedAt() {
        return updatedAt;
    }
    public void setCust_UpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String setRole(){
        return "Individual Customer";
    }
}
