package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.model.IndividualSellers;
import com.jai.croop.model.IndividualSellersProductsInventory;

import java.util.List;

public interface IIndividualSellersProductService {
    IndividualSellersProductsInventory addItems(IndividualSellersProductsInventory individualSellersProductsInventory, IndividualSellers individualSellers);
    IndividualSellersProductsInventory getItem(int id);
    List<IndividualSellersProductsInventory> findByItemName(String itemName);
    List<IndividualSellersProductsInventory> findByFirebaseID(String firebaseID);
    List<IndividualSellersProductsInventory> getAllItems();
    IndividualSellersProductsInventory updateItems(int id, IndividualSellersProductsInventory individualSellersProductsInventory);
    void deleteItems(int id);
}
