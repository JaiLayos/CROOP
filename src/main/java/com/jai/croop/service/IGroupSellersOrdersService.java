package com.jai.croop.service;

import com.jai.croop.model.Customer;
import com.jai.croop.model.CustomerOrders;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;

import java.util.List;

public interface IGroupSellersOrdersService {
    GroupSellersOrders addGroupSellerOrders(GroupSellersOrders groupSellersOrders, Customer customer, GroupSellers groupSellers);
    GroupSellersOrders getGroupSellerOrders(int id);
    List<GroupSellersOrders> getAllGroupSellerOrders();
    GroupSellersOrders updateGroupSellerOrders(int id, GroupSellersOrders updatedGroupSellersOrders);
    void deleteGroupSellerOrders(int id);
}
