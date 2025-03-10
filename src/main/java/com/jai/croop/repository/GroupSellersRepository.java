package com.jai.croop.repository;

import com.jai.croop.model.GroupSellers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupSellersRepository extends JpaRepository<GroupSellers, Integer> {
    GroupSellers findByFirebaseID(String firebaseID);
    @Query("SELECT g FROM GroupSellers g " +
            "JOIN g.groupSellersProductsInventoryList p " +
            "GROUP BY g.id " +
            "ORDER BY SUM(p.itemStart) DESC")
    List<GroupSellers> findTop3ByTotalItemStart(Pageable pageable);
    List<GroupSellers> findByGroupName(String groupName);
}
