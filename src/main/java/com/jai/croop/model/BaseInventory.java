package com.jai.croop.model;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseInventory {
    private String itemName;
    private int itemStart;
    private int itemUsed;
    private int itemRemaining;

    public BaseInventory(){

    }

    public BaseInventory(String itemName, int itemStart, int itemUsed,
                         int itemRemaining){
        this.itemName = itemName;
        this.itemStart = itemStart;
        this.itemUsed = itemUsed;
        this.itemRemaining = itemStart - itemUsed;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemStart() {
        return itemStart;
    }
    public void setItemStart(int itemStart) {
        this.itemStart = itemStart;
    }

    public int getItemUsed() {
        return itemUsed;
    }
    public void setItemUsed(int itemUsed) {
        this.itemUsed = itemUsed;
    }

    public int getItemRemaining() {
        return itemRemaining;
    }
    public void setItemRemaining(int itemRemaining) {
        this.itemRemaining = itemRemaining;
    }
}
