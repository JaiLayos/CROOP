package com.jai.croop.service;

import com.jai.croop.model.*;
import com.jai.croop.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrdersService implements ICustomerOrdersService{
    @Autowired
    private CustomerOrdersForGroupRepository customerOrdersRepository;
    @Autowired
    private CustomerOrdersForIndividualRepository customerOrdersForIndividualRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GroupSellersRepository groupSellersRepository;
    @Autowired
    private GroupSellersOrdersService groupSellersOrdersService;
    @Autowired
    private IndividualSellersRepository individualSellersRepository;
    @Autowired
    private IndividualSellersOrdersService individualSellersOrdersService;


    @Override
    public CustomerOrdersForGroupSellers addCustomerOrdersToGroupOrders(CustomerOrdersForGroupSellers customerOrders, Customer customer, GroupSellers groupSellers){
        customer = customerRepository.findById(customerOrders.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        groupSellers = groupSellersRepository.findById(customerOrders.getGroupSeller().getId())
                .orElseThrow(() -> new RuntimeException("Group Seller not found"));

        customerOrders.setCustomer(customer);
        customerOrders.setGroupSeller(groupSellers);
        CustomerOrdersForGroupSellers saved = customerOrdersRepository.save(customerOrders);
        GroupSellersOrders groupSellersOrders = new GroupSellersOrders();
        groupSellersOrders.setOrderList(saved.getOrderList());
        groupSellersOrders.setOrderPrice(saved.getOrderPrice());
        groupSellersOrders.setOrderType(saved.getOrderType());
        groupSellersOrders.setOrderStatus(saved.getOrderStatus());
        groupSellersOrders.setCustomer(customer);
        groupSellersOrders.setGroupSeller(groupSellers);
        groupSellersOrdersService.addGroupSellerOrders(groupSellersOrders,customer,groupSellers);
        return saved;
    }

    @Override
    public CustomerOrdersForIndivSellers addCustomerOrdersToIndividualOrders(CustomerOrdersForIndivSellers customerOrders, Customer customer, IndividualSellers individualSellers){
        customer = customerRepository.findById(customerOrders.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        individualSellers = individualSellersRepository.findById(customerOrders.getIndividualSellers().getId())
                .orElseThrow(() -> new RuntimeException("Individual Seller not found"));

        customerOrders.setCustomer(customer);
        customerOrders.setIndividualSellers(individualSellers);
        CustomerOrdersForIndivSellers saved = customerOrdersForIndividualRepository.save(customerOrders);
        IndividualSellersOrders individualSellersOrders = new IndividualSellersOrders();
        individualSellersOrders.setOrderList(saved.getOrderList());
        individualSellersOrders.setOrderPrice(saved.getOrderPrice());
        individualSellersOrders.setOrderType(saved.getOrderType());
        individualSellersOrders.setOrderStatus(saved.getOrderStatus());
        individualSellersOrders.setCustomer(customer);
        individualSellersOrders.setIndividualSellers(individualSellers);
        individualSellersOrdersService.addIndividualSellerOrders(individualSellersOrders,customer,individualSellers);
        return saved;
    }

    @Override
    public CustomerOrdersForGroupSellers getCustomerOrders(int id) {
        return customerOrdersRepository.findById(id).orElseThrow(() -> new RuntimeException("Order doesn't exist."));
    }

    @Override
    public List<CustomerOrdersForGroupSellers> getAllCustomerOrders() {
        return customerOrdersRepository.findAll();
    }

    @Transactional
    @Override
    public CustomerOrdersForGroupSellers updateCustomerOrders(int id, CustomerOrdersForGroupSellers updatedCustomerOrders) {
        CustomerOrdersForGroupSellers customerOrders = getCustomerOrders(id);
        customerOrders.setOrderList(updatedCustomerOrders.getOrderList());
        customerOrders.setOrderPrice(updatedCustomerOrders.getOrderPrice());
        customerOrders.setOrderStatus(updatedCustomerOrders.getOrderStatus());
        return customerOrdersRepository.save(customerOrders);
    }

    @Transactional
    @Override
    public void deleteCustomerOrders(int id) {
        customerOrdersRepository.deleteById(id);
    }
}
