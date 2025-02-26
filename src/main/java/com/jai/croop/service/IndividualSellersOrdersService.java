package com.jai.croop.service;

import com.jai.croop.model.*;
import com.jai.croop.repository.CustomerRepository;
import com.jai.croop.repository.IndividualSellersOrdersRepository;
import com.jai.croop.repository.IndividualSellersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualSellersOrdersService implements IIndividualSellersOrdersService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private IndividualSellersRepository individualSellersRepository;
    @Autowired
    private IndividualSellersOrdersRepository individualSellersOrdersRepository;
    @Autowired
    private IndividualSellersService individualSellersService;

    @Override
    public IndividualSellersOrders addIndividualSellerOrders(IndividualSellersOrders individualSellersOrders, Customer customer, IndividualSellers individualSellers) {
        customer = customerRepository.findById(individualSellersOrders.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        individualSellers = individualSellersRepository.findById(individualSellersOrders.getIndividualSellers().getId())
                .orElseThrow(() -> new RuntimeException("Group Seller not found"));
        individualSellersOrders.setCustomer(customer);
        individualSellersOrders.setIndividualSellers(individualSellers);
        return individualSellersOrdersRepository.save(individualSellersOrders);
    }

    @Override
    public IndividualSellersOrders getIndividualSellerOrders(int id) {
        return individualSellersOrdersRepository.findById(id).orElseThrow(()-> new RuntimeException("Order doesn't exist!"));
    }

    @Override
    public List<IndividualSellersOrders> getAllIndividualSellerOrders() {
        return individualSellersOrdersRepository.findAll();
    }

    @Override
    public List<SellerOrdersDTO> findByIndividualSellerID(int id) {
        List<GroupSellersOrders> orders = individualSellersOrdersRepository.findByGroupSellerId(id);

        return orders.stream()
                .map(order -> {
                    // Fetch the customer details manually
                    Customer customer = customerRepository.findById(order.getCustomer().getId())
                            .orElseThrow(() -> new RuntimeException("Customer not found"));

                    // Map to DTO
                    return new SellerOrdersDTO(
                            order.getId(),
                            order.getOrderDate(),
                            order.getOrderList(),
                            order.getOrderPrice(),
                            order.getOrderType(),
                            order.getOrderStatus(),
                            customer.getId(), // Include customer ID
                            customer.getName() // Include customer name
                    );
                })
                .toList();
    }

    @Override
    public IndividualSellersOrders updateIndividualSellerOrders(int id, IndividualSellersOrders update) {
        IndividualSellersOrders individualSellersOrders = getIndividualSellerOrders(id);
        individualSellersOrders.setOrderList(update.getOrderList());
        individualSellersOrders.setOrderPrice(update.getOrderPrice());
        individualSellersOrders.setOrderStatus(update.getOrderStatus());
        return individualSellersOrdersRepository.save(individualSellersOrders);
    }

    @Override
    public void deleteIndividualSellerOrders(int id) {
        individualSellersOrdersRepository.deleteById(id);
    }
}
