package com.jai.croop.repository;

import com.jai.croop.model.IndividualSellersProductsInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndividualSellersProductsRepository extends JpaRepository<IndividualSellersProductsInventory, Integer> {
    List<IndividualSellersProductsInventory> findByItemName(String itemName);
    List<IndividualSellersProductsInventory> findByIndividualSellersId(int groupSellerId);
}
