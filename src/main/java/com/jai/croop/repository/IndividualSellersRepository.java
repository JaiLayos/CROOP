package com.jai.croop.repository;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.IndividualSellers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualSellersRepository extends JpaRepository<IndividualSellers, Integer> {
    IndividualSellers findByFirebaseID(String firebaseID);
}
