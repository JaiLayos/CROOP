package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGroupSellersProductInventoryService {
    GroupSellersProductsInventory addItems(GroupSellersProductsInventory groupSellersProductsInventory, GroupSellers groupSellers);
    GroupSellersProductsInventory getItem(int id);
    List<GroupSellersProductsInventory> findByItemName(String itemName);
    List<GroupSellersProductsInventory> findByItemNameAndGroupSellers_Id(
            String itemName,
            int sellerId
    );
    List<GroupSellersProductsInventory> findByFirebaseID(String firebaseID);
    List<GroupSellersProductsInventory> getAllItems();
    List<GroupSellersProductsInventory> getTop3ProductsByItemStart();
    List<GroupSellersProductsInventory> findTop3ByLowestItemRemaining();
    boolean findIfSCIsSet(int id);
    boolean findIfMCIsSet(int id);
    GroupSellersProductsInventory updateItems(int id, GroupSellersProductsInventory groupSellersProductsInventory);
    void deleteItems(int id);
}
