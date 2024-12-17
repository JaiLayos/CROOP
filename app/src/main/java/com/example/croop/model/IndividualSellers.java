package com.example.croop.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class IndividualSellers extends Customer{
    private List<String> permitUrls;
    private String roles;

    public IndividualSellers (int id, String name, Map<String, String> address, String phoneNumber,
                              String email, String bio, Date createdAt, Date updatedAt, String roles,
                              List <String> permitUrls){
        super(id, name, address, phoneNumber, email, bio, createdAt, updatedAt, "Individual Seller");
        this.permitUrls = permitUrls;
        this.roles = roles;
    }

    public List<String> getPermitUrls(){
        return permitUrls;
    }
    public void setPermitUrls(){
        this.permitUrls = permitUrls;
    }
}
