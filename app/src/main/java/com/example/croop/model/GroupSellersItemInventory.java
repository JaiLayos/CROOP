package com.example.croop.model;

public class GroupSellersItemInventory extends BaseInventory{
    private int id;

    private GroupSellers groupSeller;

    public GroupSellersItemInventory(){

    }

    public GroupSellersItemInventory(GroupSellers groupSeller, String itemName, int itemStart, int itemUsed,
                                     int itemRemaining){
        super(itemName, itemStart, itemUsed,itemRemaining);
        this.groupSeller = groupSeller;
    }

    public int getId() {
        return id;
    }

    public GroupSellers getGroupSellers() {
        return groupSeller;
    }

    public void setGroupSellers(GroupSellers groupSeller) {
        this.groupSeller = groupSeller;
    }
}
