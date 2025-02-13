package com.jai.croop.repository;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupSellersRepository extends JpaRepository<GroupSellers, Integer> {
    GroupSellers findByFirebaseID(String firebaseID);
}
