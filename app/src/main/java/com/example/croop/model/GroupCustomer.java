package com.example.croop.model;

import java.util.Date;
import java.util.Map;

public class GroupCustomer extends Customer {
    private String groupName;
    private String position;
    public GroupCustomer(){

    }

    public GroupCustomer(int id, String firebaseID, String name, String password, int age, Map<String, String> address, String phoneNumber,
                    String email, String bio, Date createdAt, Date updatedAt, String messengerLink, String roles, String groupName,
                         String position, int product_inventory_SC, int product_inventory_MC){
        super(id, firebaseID, name, password, age, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, roles,
                product_inventory_SC, product_inventory_MC);
        this.groupName = groupName;
        this.position = position;
    }

    public String returnRole(){
        return "Group Customer User";
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public String getGroupName(){
        return groupName;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public String getPosition(){
        return position;
    }
}
