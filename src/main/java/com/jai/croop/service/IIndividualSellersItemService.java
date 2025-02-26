package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.IndividualSellers;
import com.jai.croop.model.IndividualSellersItemInventory;

import java.util.List;

public interface IIndividualSellersItemService {
    IndividualSellersItemInventory addItems(IndividualSellersItemInventory individualSellersItemInventory, IndividualSellers individualSellers);
    IndividualSellersItemInventory getItem(int id);
    List<IndividualSellersItemInventory> findByItemName(String itemName);
    List<IndividualSellersItemInventory> findByFirebaseID(String firebaseID);
    List<IndividualSellersItemInventory> getAllItems();
    IndividualSellersItemInventory updateItems(int id, IndividualSellersItemInventory individualSellersItemInventory);
    void deleteItems(int id);
}
