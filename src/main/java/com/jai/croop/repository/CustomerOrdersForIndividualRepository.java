package com.jai.croop.repository;

import com.jai.croop.model.CustomerOrdersForGroupSellers;
import com.jai.croop.model.CustomerOrdersForIndivSellers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrdersForIndividualRepository extends JpaRepository<CustomerOrdersForIndivSellers, Integer> {
    List<CustomerOrdersForIndivSellers> findByCustomer_Id(int id);
    List<CustomerOrdersForIndivSellers> findByIndividualSellers_Id(int id);
}

