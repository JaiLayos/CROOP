package com.jai.croop.repository;

import com.jai.croop.model.CustomerOrdersForGroupSellers;
import com.jai.croop.model.CustomerOrdersForIndivSellers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrdersForIndividualRepository extends JpaRepository<CustomerOrdersForIndivSellers, Integer> {
}

