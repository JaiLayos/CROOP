package com.jai.croop.service;

import com.jai.croop.model.Cart;

import java.util.List;

public interface ICartService {
    Cart addCart(Cart cart);
    List<Cart> getAllCart();
    Cart getCart(int id);
    List<Cart> getGroupCartBySellerID(int id);
    List<Cart> getIndividualCartBySellerID(int id);
    Cart updateCart(int id, Cart cart);
    void deleteCart(int id);
}
