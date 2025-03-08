package com.jai.croop.repository;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.IndividualSellers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndividualSellersRepository extends JpaRepository<IndividualSellers, Integer> {
    IndividualSellers findByFirebaseID(String firebaseID);
    @Query("SELECT g FROM IndividualSellers g " +
            "JOIN g.individualSellersProductsInventory p " +
            "GROUP BY g.id " +
            "ORDER BY SUM(p.itemStart) DESC")
    List<IndividualSellers> findTop3ByTotalItemStart(Pageable pageable);
    List<IndividualSellers> findByName(String name);
}
