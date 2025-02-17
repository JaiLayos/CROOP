package com.example.croop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GroupSellers extends Customer{
    private String groupName;
    private String position;
    private List<String> permitUrls;

    public GroupSellers(String firebaseID, String name, String password, int age, Map<String, String> address, String phoneNumber,
                        String email, String bio, Date createdAt, Date updatedAt, String messengerLink,
                        String roles, String groupName, String position, List<String> permitUrls){
        super(firebaseID, name, password, age, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, roles);
        this.groupName = groupName;
        this.position = position;
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
    public void setPersonPosition(String position){
        this.position = position;
    }
    public String getPersonPosition(){
        return position;
    }


    public String returnRole_assoc(){
        return "Group Business User (Association)";
    }
    public String returnRole_coop(){
        return "Group Business User (Cooperative)";
    }
}
