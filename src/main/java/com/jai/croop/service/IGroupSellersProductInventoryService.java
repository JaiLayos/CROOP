package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersProductsInventory;

import java.util.List;

public interface IGroupSellersProductInventoryService {
    GroupSellersProductsInventory addItems(GroupSellersProductsInventory groupSellersProductsInventory, GroupSellers groupSellers);
    GroupSellersProductsInventory getItem(int id);
    List<GroupSellersProductsInventory> findByItemName(String itemName);
    List<GroupSellersProductsInventory> getAllItems();
    GroupSellersProductsInventory updateItems(int id, GroupSellersProductsInventory groupSellersProductsInventory);
    void deleteItems(int id);
}
