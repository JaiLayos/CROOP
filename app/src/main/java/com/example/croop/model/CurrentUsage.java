package com.example.croop.model;

public class CurrentUsage {
    String currentUsage;
    String population;

    public CurrentUsage(){

    }

    public CurrentUsage(String currentUsage, String population){
        this.currentUsage = currentUsage;
    }

    public void setCurrentUsage(String currentUsage){
        this.currentUsage=currentUsage;
    }

    public String getCurrentUsage() {
        return currentUsage;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getPopulation() {
        return population;
    }
}
