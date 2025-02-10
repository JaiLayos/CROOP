package com.jai.croop.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.croop.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}