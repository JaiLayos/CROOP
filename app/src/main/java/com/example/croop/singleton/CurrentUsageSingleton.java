package com.example.croop.singleton;

import com.example.croop.model.CurrentUsage;

public class CurrentUsageSingleton {
    public static CurrentUsageSingleton instance;
    private CurrentUsage cUsage;

    private CurrentUsageSingleton() {

    }

    public static synchronized CurrentUsageSingleton getInstance(){
        if(instance==null){
            instance = new CurrentUsageSingleton();
        }
        return instance;
    }

    public void setCurrentUsageSingleton(CurrentUsage cUsage){
        this.cUsage = cUsage;
    }

    public CurrentUsage getCurrentUsageSingleton(){
        return cUsage;
    }

}
