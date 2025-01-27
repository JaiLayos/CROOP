package com.example.croop.singleton;

import com.example.croop.model.GroupSellers;

public class GroupSellersSingleton {

    private static GroupSellersSingleton instance;
    private GroupSellers groupSellers;
    private GroupSellersSingleton(){

    }

    public static synchronized GroupSellersSingleton getInstance(){
        if (instance==null){
            instance = new GroupSellersSingleton();
        }
        return instance;
    }

    public void setGroupSellers(GroupSellers groupSellers){
        this.groupSellers = groupSellers;
    }
    public GroupSellers getGroupSellers(){
        return groupSellers;
    }

}
