package com.example.croop.singleton;

import com.example.croop.model.GroupCustomer;

public class GroupCustomerSingleton {
    private static GroupCustomerSingleton instance;
    private GroupCustomer groupCustomer;
    private GroupCustomerSingleton(){

    }
    public static synchronized GroupCustomerSingleton getInstance(){
        if(instance==null){
            instance = new GroupCustomerSingleton();
        }
        return instance;
    }

    public void setGroupCustomer(GroupCustomer groupCustomer){
        this.groupCustomer = groupCustomer;
    }

    public GroupCustomer getGroupCustomer(){
        return groupCustomer;
    }
}
