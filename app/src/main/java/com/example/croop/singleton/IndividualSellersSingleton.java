package com.example.croop.singleton;

import com.example.croop.model.IndividualSellers;

public class IndividualSellersSingleton {
    public static IndividualSellersSingleton instance;
    public IndividualSellers iSellers;

    private IndividualSellersSingleton(){};

    public static synchronized IndividualSellersSingleton getInstance(){
        if(instance==null){
            instance = new IndividualSellersSingleton();
        }
        return instance;
    }

    public IndividualSellers getIndividualSellers() {
        return iSellers;
    }

    public void setIndividualSellers(IndividualSellers iSellers){
        this.iSellers = iSellers;
    }
}
