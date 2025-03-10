package com.jai.croop.service;

import com.jai.croop.model.Cart;
import com.jai.croop.model.CartGroupedResponseDTO;

import java.util.List;

public interface ICartService {
    Cart addCart(Cart cart);
    List<Cart> getAllCart();
    Cart getCart(int id);
    List<Cart> getGroupCartBySellerID(int id);
    List<Cart> getIndividualCartBySellerID(int id);
    CartGroupedResponseDTO getGroupedCartByCustomer(int customerId);
    Cart updateCart(int id, Cart cart);
    void deleteCart(int id);
    Cart findByCustomerAndCrop(int customerId, int cropId); // Add this
}
