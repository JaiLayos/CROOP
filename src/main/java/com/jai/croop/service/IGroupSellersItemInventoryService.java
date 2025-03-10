package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;

import java.util.List;

public interface IGroupSellersItemInventoryService {
    GroupSellersItemInventory addItems(GroupSellersItemInventory groupSellersItemInventory, GroupSellers groupSellers);
    GroupSellersItemInventory getItem(int id);
    List<GroupSellersItemInventory> findByItemNameAndGroupSellers_Id(
            String itemName,
            int sellerId
    );
    List<GroupSellersItemInventory> findByItemName(String itemName);
    List<GroupSellersItemInventory> findByFirebaseID(String firebaseID);
    List<GroupSellersItemInventory> getAllItems();
    GroupSellersItemInventory updateItems(int id, GroupSellersItemInventory groupSellersItemInventory);
    void deleteItems(int id);
}
