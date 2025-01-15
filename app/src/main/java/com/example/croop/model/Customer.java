package com.example.croop.model;

import java.util.Date;
import java.util.Map;

public class Customer {
    private int id;
    private String name;
    private int age;
    private Map<String, String> address;
    private String phoneNumber;
    private String email;
    private String bio;
    private Date createdAt;
    private Date updatedAt;
    private String messengerLink;
    private String roles;

    public Customer(){

    }

    public Customer(int id, String name, int age, Map<String, String> address, String phoneNumber,
     String email, String bio, Date createdAt, Date updatedAt, String messengerLink, String roles){
        this.age = age;
        this.id = id;
        this.name =  name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messengerLink = messengerLink;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age=age;
    }

    public Map<String, String> getAddress(){
        return address;
    }
    public void setAddress(Map<String, String> address) {
        this.address = address;
    }

    public String getPhoneNum(){
        return phoneNumber;
    }
    public void setPhoneNum(String phoneNumber) {
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

    public String getMessengerLink() {
        return messengerLink;
    }
    public void setMessengerLink(String messengerLink) {
        this.messengerLink = messengerLink;
    }

    public String setRole(){
        return "Individual Customer";
    }
}
