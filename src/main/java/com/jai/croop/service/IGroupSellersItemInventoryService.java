package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;

import java.util.List;

public interface IGroupSellersItemInventoryService {
    GroupSellersItemInventory addItems(GroupSellersItemInventory groupSellersItemInventory, GroupSellers groupSellers);
    GroupSellersItemInventory getItem(int id);
    GroupSellersItemInventory findByItemName(String itemName);
    List<GroupSellersItemInventory> getAllItems();
    GroupSellersItemInventory updateItems(int id, GroupSellersItemInventory groupSellersItemInventory);
    void deleteItems(int id);
}
