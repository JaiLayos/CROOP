package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupSellersItemRepository extends JpaRepository<GroupSellersItemInventory, Integer> {
    GroupSellersItemInventory findByItemName(String itemName);
}
