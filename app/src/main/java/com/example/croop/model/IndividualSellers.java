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
                              String roles, List <String> permitUrls, int product_inventory_SC, int product_inventory_MC){
        super(id, firebaseID, name, password, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, roles,
                product_inventory_SC, product_inventory_MC);
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
