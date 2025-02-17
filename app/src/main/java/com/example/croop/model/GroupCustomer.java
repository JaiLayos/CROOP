package com.example.croop.model;

import java.util.Date;
import java.util.Map;

public class GroupCustomer extends Customer {
    private String groupName;
    private String position;
    public GroupCustomer(){

    }

    public GroupCustomer(String firebaseID, String name, String password, int age, Map<String, String> address, String phoneNumber,
                    String email, String bio, Date createdAt, Date updatedAt, String messengerLink, String roles, String groupName,
                         String position){
        super(firebaseID, name, password, age, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, roles);
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
