package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.model.IndividualSellers;
import com.jai.croop.model.IndividualSellersProductsInventory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IIndividualSellersProductService {
    IndividualSellersProductsInventory addItems(IndividualSellersProductsInventory individualSellersProductsInventory, IndividualSellers individualSellers);
    IndividualSellersProductsInventory getItem(int id);
    List<IndividualSellersProductsInventory> findByItemName(String itemName);
    List<IndividualSellersProductsInventory> findByFirebaseID(String firebaseID);
    List<IndividualSellersProductsInventory> getAllItems();
    List<IndividualSellersProductsInventory> findTop3ByItemStart();
    List<IndividualSellersProductsInventory> findTop3ByLowestItemRemaining();
    IndividualSellersProductsInventory updateItems(int id, IndividualSellersProductsInventory individualSellersProductsInventory);
    void deleteItems(int id);
}
