package com.example.croop.singleton;

import com.example.croop.model.CurrentRole;

public class CurrentUserSingleton {

    public static CurrentUserSingleton instance;
    private CurrentRole currentRole;

    private CurrentUserSingleton() {}
    public static synchronized CurrentUserSingleton getInstance(){
        if(instance==null){
            instance = new CurrentUserSingleton();
        }
        return instance;
    }

    public CurrentRole getCurrentRole(){
        return currentRole;
    }

    public void setCurrentRole(CurrentRole currentRole) {
        this.currentRole = currentRole;
    }
}
