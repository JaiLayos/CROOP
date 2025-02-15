package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersProductsInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupSellersProductsRepository extends JpaRepository<GroupSellersProductsInventory, Integer> {
    List<GroupSellersProductsInventory> findByItemName(String itemName);
}
