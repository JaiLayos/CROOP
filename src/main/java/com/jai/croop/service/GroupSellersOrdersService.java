package com.jai.croop.service;

import com.jai.croop.model.Customer;
import com.jai.croop.model.SellerOrdersDTO;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import com.jai.croop.repository.CustomerRepository;
import com.jai.croop.repository.GroupSellersOrdersRepository;
import com.jai.croop.repository.GroupSellersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSellersOrdersService implements IGroupSellersOrdersService{
    @Autowired
    private GroupSellersRepository groupSellersRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GroupSellersOrdersRepository groupSellersOrdersRepository;
    @Autowired
    private GroupSellersService groupSellersService;

    @Override
    public GroupSellersOrders addGroupSellerOrders(GroupSellersOrders groupSellersOrders, Customer customer, GroupSellers groupSellers) {
        customer = customerRepository.findById(groupSellersOrders.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        groupSellers = groupSellersRepository.findById(groupSellersOrders.getGroupSeller().getId())
                .orElseThrow(() -> new RuntimeException("Group Seller not found"));
        groupSellersOrders.setCustomer(customer);
        groupSellersOrders.setGroupSeller(groupSellers);
        return groupSellersOrdersRepository.save(groupSellersOrders);
    }

    @Override
    public GroupSellersOrders getGroupSellerOrders(int id) {
        return groupSellersOrdersRepository.findById(id).orElseThrow(()-> new RuntimeException("Order doesn't exist!"));
    }

    @Override
    public List<GroupSellersOrders> getAllGroupSellerOrders() {
        return groupSellersOrdersRepository.findAll();
    }

    @Override
    public List<SellerOrdersDTO> findByGroupSellerID(int id) {
        List<GroupSellersOrders> orders = groupSellersOrdersRepository.findByGroupSellerId(id);

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
    public GroupSellersOrders updateGroupSellerOrders(int id, GroupSellersOrders updatedGroupSellersOrders) {
        GroupSellersOrders groupSellersOrders = getGroupSellerOrders(id);
        groupSellersOrders.setOrderList(updatedGroupSellersOrders.getOrderList());
        groupSellersOrders.setOrderPrice(updatedGroupSellersOrders.getOrderPrice());
        groupSellersOrders.setOrderStatus(updatedGroupSellersOrders.getOrderStatus());
        return groupSellersOrdersRepository.save(groupSellersOrders);
    }

    @Override
    public void deleteGroupSellerOrders(int id) {
        groupSellersOrdersRepository.deleteById(id);
    }
}
