package com.jai.croop.service;

import com.jai.croop.model.*;
import com.jai.croop.repository.CustomerOrdersForIndividualRepository;

import java.util.List;

public interface ICustomerOrdersService {
    CustomerOrdersForGroupSellers addCustomerOrdersToGroupOrders(CustomerOrdersForGroupSellers customerOrders, int customerID, int groupSellerID);
    CustomerOrdersForIndivSellers addCustomerOrdersToIndividualOrders(CustomerOrdersForIndivSellers customerOrders, int customerID, int individualSellerID);
    CustomerOrdersForGroupSellers getCustomerOrdersFromGroup(int id);
    List<CustomerOrdersForGroupSellers> getAllCustomerOrdersFromGroup();
    List<CustomerOrdersForGroupSellers> groupSellersFindByCustomerId(int id);
    List<CustomerOrdersForGroupSellers> findByGroupSellerId(int id);
    List<CustomerOrdersForIndivSellers> individualSellersFindByCustomerId(int id);
    List<CustomerOrdersForIndivSellers> findByindividualSellerId(int id);
    CustomerOrdersForIndivSellers getCustomerOrdersFromIndividual(int id);
    List<CustomerOrdersForIndivSellers> getAllCustomerOrdersFromIndividual();
    CustomerOrdersForGroupSellers updateCustomerOrdersFromGroup(int id, CustomerOrdersForGroupSellers updatedCustomerOrders);
    CustomerOrdersForIndivSellers updateCustomerOrdersFromIndividual(int id, CustomerOrdersForIndivSellers updatedCustomerOrders);
    void deleteGroupCustomerOrders(int id);
    void deleteIndividualCustomerOrders(int id);
    List<DailySalesDTO> getDailySalesForGroupSeller(int sellerId);
    List<Integer> getPastOrderQuantities(int groupSellerId, int productId);
}
