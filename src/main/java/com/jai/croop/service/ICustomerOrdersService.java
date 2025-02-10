package com.jai.croop.service;

import com.jai.croop.model.Customer;
import com.jai.croop.model.CustomerOrders;
import com.jai.croop.model.GroupSellers;

import java.util.List;

public interface ICustomerOrdersService {
    CustomerOrders addCustomerOrdersToGroupOrders(CustomerOrders customerOrders, Customer customer, GroupSellers groupSellers);
    CustomerOrders getCustomerOrders(int id);
    List<CustomerOrders> getAllCustomerOrders();
    CustomerOrders updateCustomerOrders(int id, CustomerOrders updatedCustomerOrders);
    void deleteCustomerOrders(int id);

}
