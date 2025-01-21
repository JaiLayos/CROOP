package com.example.croop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GroupSellers extends Customer{
    private String groupName;
    private List<String> permitUrls;

    public GroupSellers(int id, String name, String password, int age, Map<String, String> address, String phoneNumber,
                        String email, String bio, Date createdAt, Date updatedAt, String groupName, String messengerLink,
                        String roles, List<String> permitUrls){
        super(id, name, password, age, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, "Group Business User");
        this.groupName = groupName;
        this.permitUrls = new ArrayList<>(permitUrls);
    }

    public GroupSellers(){

    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    public String getGroupName(){
        return groupName;
    }
    public void setPermitUrls(List<String> permitUrls){
        this.permitUrls = new ArrayList<>(permitUrls);
    }
    public List<String> getPermitUrls(){
        return permitUrls;
    }
}
