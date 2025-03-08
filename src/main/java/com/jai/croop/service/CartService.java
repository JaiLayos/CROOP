package com.jai.croop.service;

import com.jai.croop.model.Cart;
import com.jai.croop.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICartService{
    @Autowired
    private CartRepository cartRepository;
    @Override
    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getAllCart() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCart(int id) {
        return cartRepository.findById(id).orElseThrow(()->
                new RuntimeException("Cart doesn't exist!"));
    }

    @Override
    public List<Cart> getGroupCartBySellerID(int id) {
        return cartRepository.findByGroupSellersId(id);
    }

    @Override
    public List<Cart> getIndividualCartBySellerID(int id) {
        return cartRepository.findByIndividualSellersId(id);
    }

    @Override
    @Transactional
    public Cart updateCart(int id, Cart cart) {
        Cart current = getCart(id);
        current.setId(cart.getCropID());
        current.setCropName(cart.getCropName());
        current.setQuantity(cart.getQuantity());
        current.setPrice(cart.getPrice());
        current.setGroupSellers(cart.getGroupSellers());
        current.setIndividualSellers(cart.getIndividualSellers());
        current.setCustomer(cart.getCustomer());
        return cartRepository.save(current);
    }

    @Override
    @Transactional
    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }
}
