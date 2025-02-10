package com.jai.croop.service;

import com.jai.croop.model.Customer;
import com.jai.croop.model.CustomerOrders;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import com.jai.croop.repository.CustomerOrdersRepository;
import com.jai.croop.repository.CustomerRepository;
import com.jai.croop.repository.GroupSellersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrdersService implements ICustomerOrdersService{
    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GroupSellersRepository groupSellersRepository;
    @Autowired
    private GroupSellersOrdersService groupSellersOrdersService;


    @Override
    public CustomerOrders addCustomerOrdersToGroupOrders(CustomerOrders customerOrders, Customer customer, GroupSellers groupSellers){
        customer = customerRepository.findById(customerOrders.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        groupSellers = groupSellersRepository.findById(customerOrders.getGroupSeller().getId())
                .orElseThrow(() -> new RuntimeException("Group Seller not found"));

        customerOrders.setCustomer(customer);
        customerOrders.setGroupSeller(groupSellers);
        CustomerOrders saved = customerOrdersRepository.save(customerOrders);
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
    public CustomerOrders getCustomerOrders(int id) {
        return customerOrdersRepository.findById(id).orElseThrow(() -> new RuntimeException("Order doesn't exist."));
    }

    @Override
    public List<CustomerOrders> getAllCustomerOrders() {
        return customerOrdersRepository.findAll();
    }

    @Transactional
    @Override
    public CustomerOrders updateCustomerOrders(int id, CustomerOrders updatedCustomerOrders) {
        CustomerOrders customerOrders = getCustomerOrders(id);
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
