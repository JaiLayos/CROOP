package com.example.croop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GroupSellers extends Customer{
    private String groupName;
    private String personPosition;
    private List<String> permitUrls;

    public GroupSellers(int id, String name, String password, int age, Map<String, String> address, String phoneNumber,
                        String email, String bio, Date createdAt, Date updatedAt, String messengerLink,
                        String roles, String groupName, String personPosition, List<String> permitUrls){
        super(id, name, password, age, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, roles);
        this.groupName = groupName;
        this.personPosition = personPosition;
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
    public void setPersonPosition(String personPosition){
        this.personPosition = personPosition;
    }
    public String getPersonPosition(){
        return personPosition;
    }

    public String returnRole(){
        return "Group Business User";
    }
}
