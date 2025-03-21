package com.example.croop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GroupSellers extends Customer{
    private String groupName;
    private String position;
    private List<String> permitUrls;
    private List<GroupSellersOrders> groupSellerOrders;
    private List<GroupSellersItemInventory> groupSellersItemInventoryList;
    private List<GroupSellersProductsInventory> groupSellersProductsInventoryList;
    private List<GroupSellersDiscount> groupSellerDiscounts;

    public GroupSellers(int id, String firebaseID, String name, String password, int age, Map<String, String> address, String phoneNumber,
                        String email, String bio, Date createdAt, Date updatedAt, String messengerLink,
                        String roles, String groupName, String position, List<String> permitUrls,
                        List<GroupSellersOrders> groupSellerOrders, List<GroupSellersItemInventory> groupSellersItemInventoryList,
                        List<GroupSellersProductsInventory> groupSellersProductsInventoryList,
                        List<GroupSellersDiscount> groupSellerDiscounts,
                        int product_inventory_SC, int product_inventory_MC){
        super(id, firebaseID, name, password, age, address, phoneNumber, email, bio, createdAt, updatedAt, messengerLink, roles,
                product_inventory_SC, product_inventory_MC);
        this.groupName = groupName;
        this.position = position;
        this.permitUrls = new ArrayList<>(permitUrls);
        this.groupSellerOrders = groupSellerOrders;
        this.groupSellersItemInventoryList = groupSellersItemInventoryList;
        this.groupSellersProductsInventoryList = groupSellersProductsInventoryList;
        this.groupSellerDiscounts = groupSellerDiscounts;
    }

    public GroupSellers(){

    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    public String getGroupName(){
        return groupName;
    }
    public void setPermitUrls(List<String> permitUrls){
        this.permitUrls = new ArrayList<>(permitUrls);
    }
    public List<String> getPermitUrls(){
        return permitUrls;
    }
    public void setPersonPosition(String position){
        this.position = position;
    }
    public String getPersonPosition(){
        return position;
    }

    // Additional getters and setters
    public void setGroupSellerOrders(List<GroupSellersOrders> groupSellerOrders) {
        this.groupSellerOrders = groupSellerOrders;
    }

    public List<GroupSellersOrders> getGroupSellerOrders() {
        return groupSellerOrders;
    }

    public void setGroupSellersItemInventoryList(List<GroupSellersItemInventory> groupSellersItemInventoryList) {
        this.groupSellersItemInventoryList = groupSellersItemInventoryList;
    }

    public List<GroupSellersItemInventory> getGroupSellersItemInventoryList() {
        return groupSellersItemInventoryList;
    }

    public void setGroupSellersProductsInventoryList(List<GroupSellersProductsInventory> groupSellersProductsInventoryList) {
        this.groupSellersProductsInventoryList = groupSellersProductsInventoryList;
    }

    public List<GroupSellersProductsInventory> getGroupSellersProductsInventoryList() {
        return groupSellersProductsInventoryList;
    }

    public void setGroupSellerDiscounts(List<GroupSellersDiscount> groupSellerDiscounts) {
        this.groupSellerDiscounts = groupSellerDiscounts;
    }

    public List<GroupSellersDiscount> getGroupSellerDiscounts() {
        return groupSellerDiscounts;
    }

    public String returnRole_assoc(){
        return "Group Business User (Association)";
    }
    public String returnRole_coop(){
        return "Group Business User (Cooperative)";
    }
}
