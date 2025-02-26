package com.jai.croop.repository;

import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.IndividualSellersDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndividualSellersDiscountRepository extends JpaRepository<IndividualSellersDiscount, Integer> {
    @Query("SELECT isd FROM IndividualSellersDiscount isd " +
            "JOIN isd.individualSellersProductsInventory ispi " +
            "WHERE ispi.itemName = :itemName")
    List<IndividualSellersDiscount> findByItemName(String itemName);
    List<IndividualSellersDiscount> findByIndividualSellersId(int id);
}
