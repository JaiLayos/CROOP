package com.jai.croop.repository;

import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellersProductsInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupSellersDiscountRepository extends JpaRepository<GroupSellerDiscount, Integer> {
    @Query("SELECT gsd FROM GroupSellerDiscount gsd " +
            "JOIN gsd.groupSellersProductsInventory gspi " +
            "WHERE gspi.itemName = :itemName")
    List<GroupSellerDiscount> findByItemName(String itemName);
}
