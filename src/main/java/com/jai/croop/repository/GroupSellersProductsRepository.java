package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupSellersProductsRepository extends JpaRepository<GroupSellersProductsInventory, Integer> {
    List<GroupSellersProductsInventory> findByItemNameAndGroupSellers_Id(
            String itemName,
            int sellerId
    );
    List<GroupSellersProductsInventory> findByItemName(String itemName);
    List<GroupSellersProductsInventory> findByGroupSellersId(int groupSellerId);
    @Query("SELECT g FROM GroupSellersProductsInventory g ORDER BY g.itemStart DESC")
    List<GroupSellersProductsInventory> findTop3ByItemStart(Pageable pageable);
    @Query("SELECT g FROM GroupSellersProductsInventory g WHERE g.itemRemaining > 0 ORDER BY g.itemRemaining ASC")
    List<GroupSellersProductsInventory> findTop3ByLowestItemRemaining(Pageable pageable);
}
