package com.jai.croop.repository;

import com.jai.croop.model.GroupSellersOrders;
import com.jai.croop.model.IndividualSellersOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IndividualSellersOrdersRepository extends JpaRepository<IndividualSellersOrders, Integer> {
    @Query("SELECT o FROM IndividualSellersOrders o WHERE o.individualSellers.id = :individualSellersId")
    List<GroupSellersOrders> findByGroupSellerId(@Param("individualSellersId") int id);
}
