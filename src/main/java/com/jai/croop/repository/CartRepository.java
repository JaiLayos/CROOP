package com.jai.croop.repository;

import com.jai.croop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByGroupSellersId(int id);
    List<Cart> findByIndividualSellersId(int id);
    List<Cart> findByCustomerId(int id);
    @Query("SELECT c FROM Cart c WHERE c.customer.id = :customerId AND c.cropID = :cropId")
    Cart findByCustomerIdAndCropID(@Param("customerId") int customerId, @Param("cropId") int cropId);
}
