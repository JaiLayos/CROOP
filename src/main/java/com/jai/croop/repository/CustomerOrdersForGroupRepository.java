package com.jai.croop.repository;

import com.jai.croop.model.CustomerOrdersForGroupSellers;
import com.jai.croop.model.DailySalesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerOrdersForGroupRepository extends JpaRepository<CustomerOrdersForGroupSellers, Integer> {
    List<CustomerOrdersForGroupSellers> findByCustomer_Id(int id);
    List<CustomerOrdersForGroupSellers> findByGroupSeller_Id(int id);
    @Query("SELECT NEW com.jai.croop.model.DailySalesDTO(" +
            "  CAST(o.orderDate AS DATE), " +
            "  SUM(o.orderPrice * 1.0)) " +
            "FROM CustomerOrdersForGroupSellers o " +
            "WHERE o.groupSeller.id = :sellerId " +
            "GROUP BY CAST(o.orderDate AS DATE) " +
            "ORDER BY CAST(o.orderDate AS DATE)")
    List<DailySalesDTO> findDailySalesByGroupSellerId(@Param("sellerId") int sellerId);

}
