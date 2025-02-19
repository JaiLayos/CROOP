package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupSellersItemRepository extends JpaRepository<GroupSellersItemInventory, Integer> {
    List<GroupSellersItemInventory> findByItemName(String itemName);
    List<GroupSellersItemInventory> findByGroupSellerId(int groupSellerId);

}
