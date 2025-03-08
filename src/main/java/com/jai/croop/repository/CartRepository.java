package com.jai.croop.repository;

import com.jai.croop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByGroupSellersId(int id);
    List<Cart> findByIndividualSellersId(int id);
}
