package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.model.IndividualSellersProductsInventory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndividualSellersProductsRepository extends JpaRepository<IndividualSellersProductsInventory, Integer> {
    List<IndividualSellersProductsInventory> findByItemName(String itemName);
    List<IndividualSellersProductsInventory> findByIndividualSellersId(int groupSellerId);
    @Query("SELECT g FROM IndividualSellersProductsInventory g ORDER BY g.itemStart DESC")
    List<IndividualSellersProductsInventory> findTop3ByItemStart(Pageable pageable);
    @Query("SELECT g FROM IndividualSellersProductsInventory g WHERE g.itemRemaining > 0 ORDER BY g.itemRemaining ASC")
    List<IndividualSellersProductsInventory> findTop3ByLowestItemRemaining(Pageable pageable);
}
