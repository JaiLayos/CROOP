package com.jai.croop.repository;

import com.jai.croop.model.CustomerOrdersForGroupSellers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrdersForGroupRepository extends JpaRepository<CustomerOrdersForGroupSellers, Integer> {
}
