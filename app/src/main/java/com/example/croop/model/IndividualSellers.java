package com.example.croop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class IndividualSellers extends Customer{
    private List<String> permitUrls;

    public IndividualSellers(){

    }
    public IndividualSellers (int id, String firebaseID, String name, String password, int age, Map<String, String> address, String phoneNumber,
                              String email, String bio, Date createdAt, Date updatedAt, String messengerLink,
                              String roles, List <String> permitUrls){
        super(id, firebaseID, name, password, age, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, roles);
        this.permitUrls = new ArrayList<>(permitUrls);
    }

    public List<String> getPermitUrls(){
        return permitUrls;
    }
    public void setPermitUrls(List<String> permitUrls){
        this.permitUrls = permitUrls;
    }
    public String returnRole(){
        return "Individual Business User";
    }
}
