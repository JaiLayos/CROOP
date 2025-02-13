package com.jai.croop.repository;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface GroupSellersOrdersRepository extends JpaRepository<GroupSellersOrders, Integer> {
    GroupSellersOrders findByGroupSellerId(int id);
}
