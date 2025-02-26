package com.jai.croop.service;

import com.jai.croop.model.*;

import java.util.List;

public interface ICustomerOrdersService {
    CustomerOrdersForGroupSellers addCustomerOrdersToGroupOrders(CustomerOrdersForGroupSellers customerOrders, Customer customer, GroupSellers groupSellers);

    CustomerOrdersForIndivSellers addCustomerOrdersToIndividualOrders(CustomerOrdersForIndivSellers customerOrders, Customer customer, IndividualSellers individualSellers);

    CustomerOrdersForGroupSellers getCustomerOrders(int id);
    List<CustomerOrdersForGroupSellers> getAllCustomerOrders();
    CustomerOrdersForGroupSellers updateCustomerOrders(int id, CustomerOrdersForGroupSellers updatedCustomerOrders);
    void deleteCustomerOrders(int id);

}
