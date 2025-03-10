package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.model.IndividualSellersItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupSellersItemRepository extends JpaRepository<GroupSellersItemInventory, Integer> {
    List<GroupSellersItemInventory> findByItemNameAndGroupSeller_Id(
            String itemName,
            int sellerId
    );
    List<GroupSellersItemInventory> findByItemName(String itemName);
    List<GroupSellersItemInventory> findByGroupSellerId(int groupSellerId);

}
