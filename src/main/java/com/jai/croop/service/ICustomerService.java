package com.jai.croop.service;

import java.util.List;

import com.jai.croop.model.Customer;

public interface ICustomerService {
    List<Customer> getAllCustomers();
    Customer addCustomer(Customer customer);
    Customer getCustomer(int id);
    Customer findByFirebaseID(String firebaseID);
    Customer updateCustomer(int id, Customer customer);
    void  deleteCustomer(int id);
}