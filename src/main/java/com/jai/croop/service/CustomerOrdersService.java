package com.jai.croop.service;

import com.jai.croop.model.*;
import com.jai.croop.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrdersService implements ICustomerOrdersService{
    @Autowired
    private CustomerOrdersForGroupRepository customerOrdersForGroupRepository;
    @Autowired
    private CustomerOrdersForIndividualRepository customerOrdersForIndividualRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GroupSellersRepository groupSellersRepository;
    @Autowired
    private IndividualSellersRepository individualSellersRepository;
    @Autowired
    private GroupSellersProductService groupSellersProductInventoryService;




    @Override
    public CustomerOrdersForGroupSellers addCustomerOrdersToGroupOrders(CustomerOrdersForGroupSellers customerOrders, int customerID, int groupSellerID){
        Customer customer = customerRepository.findById(customerID)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        GroupSellers groupSellers = groupSellersRepository.findById(groupSellerID)
                .orElseThrow(() -> new RuntimeException("Group Seller not found"));

        customerOrders.setCustomer(customer);
        customerOrders.setGroupSeller(groupSellers);
        CustomerOrdersForGroupSellers saved = customerOrdersForGroupRepository.save(customerOrders);
        return saved;
    }

    @Override
    public CustomerOrdersForIndivSellers addCustomerOrdersToIndividualOrders(CustomerOrdersForIndivSellers customerOrders, int customerID, int individualSellersID){
        Customer customer = customerRepository.findById(customerID)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        IndividualSellers individualSellers = individualSellersRepository.findById(individualSellersID)
                .orElseThrow(() -> new RuntimeException("Individual Seller not found"));

        customerOrders.setCustomer(customer);
        customerOrders.setIndividualSellers(individualSellers);
        CustomerOrdersForIndivSellers saved = customerOrdersForIndividualRepository.save(customerOrders);
        return saved;
    }

    @Override
    public CustomerOrdersForGroupSellers getCustomerOrdersFromGroup(int id) {
        return customerOrdersForGroupRepository.findById(id).orElseThrow(() -> new RuntimeException("Order doesn't exist."));
    }

    @Override
    public CustomerOrdersForIndivSellers getCustomerOrdersFromIndividual(int id) {
        return customerOrdersForIndividualRepository.findById(id).orElseThrow(() -> new RuntimeException("Order doesn't exist."));
    }

    @Override
    public List<CustomerOrdersForGroupSellers> getAllCustomerOrdersFromGroup() {
        return customerOrdersForGroupRepository.findAll();
    }

    @Override
    public List<CustomerOrdersForGroupSellers> groupSellersFindByCustomerId(int id) {
        return customerOrdersForGroupRepository.findByCustomer_Id(id);
    }

    @Override
    public List<CustomerOrdersForGroupSellers> findByGroupSellerId(int id) {
        return customerOrdersForGroupRepository.findByGroupSeller_Id(id);
    }

    @Override
    public List<CustomerOrdersForIndivSellers> individualSellersFindByCustomerId(int id) {
        return customerOrdersForIndividualRepository.findByCustomer_Id(id);
    }

    @Override
    public List<CustomerOrdersForIndivSellers> findByindividualSellerId(int id) {
        return customerOrdersForIndividualRepository.findByIndividualSellers_Id(id);
    }


    @Override
    public List<CustomerOrdersForIndivSellers> getAllCustomerOrdersFromIndividual() {
        return null;
    }

    @Transactional
    @Override
    public CustomerOrdersForGroupSellers updateCustomerOrdersFromGroup(int id, CustomerOrdersForGroupSellers updatedCustomerOrders) {
        CustomerOrdersForGroupSellers customerOrders = getCustomerOrdersFromGroup(id);
        customerOrders.setOrderList(updatedCustomerOrders.getOrderList());
        customerOrders.setOrderPrice(updatedCustomerOrders.getOrderPrice());
        customerOrders.setOrderStatus(updatedCustomerOrders.getOrderStatus());
        return customerOrdersForGroupRepository.save(customerOrders);
    }

    @Override
    public CustomerOrdersForIndivSellers updateCustomerOrdersFromIndividual(int id, CustomerOrdersForIndivSellers updatedCustomerOrders) {
        CustomerOrdersForIndivSellers customerOrders = getCustomerOrdersFromIndividual(id);
        customerOrders.setOrderList(updatedCustomerOrders.getOrderList());
        customerOrders.setOrderPrice(updatedCustomerOrders.getOrderPrice());
        customerOrders.setOrderStatus(updatedCustomerOrders.getOrderStatus());
        return customerOrdersForIndividualRepository.save(customerOrders);    }

    @Transactional
    @Override
    public void deleteGroupCustomerOrders(int id) {
        customerOrdersForGroupRepository.deleteById(id);
    }

    @Override
    public void deleteIndividualCustomerOrders(int id) {
        customerOrdersForIndividualRepository.deleteById(id);
    }

    @Override
    public List<DailySalesDTO> getDailySalesForGroupSeller(int sellerId) {
        return customerOrdersForGroupRepository.findDailySalesByGroupSellerId(sellerId);
    }

    @Override
    public List<Integer> getPastOrderQuantities(int groupSellerId, int productId) {
        List<CustomerOrdersForGroupSellers> pastOrders = customerOrdersForGroupRepository.findByGroupSeller_Id(groupSellerId);

        List<Integer> demand = new ArrayList<>();
        for (CustomerOrdersForGroupSellers order : pastOrders) {
            Map<String, Integer> orderList = order.getOrderList();
            if (orderList != null) {
                for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
                    if (groupSellersProductInventoryService.findByItemName(entry.getKey())
                            .stream().anyMatch(p -> p.getId() == productId)) {
                        demand.add(entry.getValue());
                    }
                }
            }
        }
        return demand;
    }
}
