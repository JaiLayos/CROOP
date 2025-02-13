package com.jai.croop.repository;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GroupSellersOrdersRepository extends JpaRepository<GroupSellersOrders, Integer> {
    @Query("SELECT o FROM GroupSellersOrders o WHERE o.groupSeller.id = :groupSellerId")
    List<GroupSellersOrders> findByGroupSellerId(@Param("groupSellerId") int id);
}
