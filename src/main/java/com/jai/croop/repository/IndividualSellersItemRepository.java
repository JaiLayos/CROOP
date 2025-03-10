package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.model.IndividualSellersItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndividualSellersItemRepository extends JpaRepository<IndividualSellersItemInventory, Integer> {
    List<IndividualSellersItemInventory> findByItemNameAndIndividualSellers_Id(
            String itemName,
            int sellerId
    );
    List<IndividualSellersItemInventory> findByItemName(String itemName);
    List<IndividualSellersItemInventory> findByIndividualSellers_Id(int id);
}
