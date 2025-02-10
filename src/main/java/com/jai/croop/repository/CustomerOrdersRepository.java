package com.jai.croop.repository;

import com.jai.croop.model.Customer;
import com.jai.croop.model.CustomerOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrdersRepository extends JpaRepository<CustomerOrders, Integer> {
}
