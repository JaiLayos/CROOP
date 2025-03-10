package com.jai.croop.service;

import com.jai.croop.model.*;
import com.jai.croop.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService{
    @Autowired
    private CartRepository cartRepository;
    @Override
    @Transactional
    public Cart addCart(Cart cart) {
        if (cart.getCustomer() == null ||
                (cart.getGroupSellers() == null && cart.getIndividualSellers() == null)) {
            throw new IllegalArgumentException("Customer and seller are required");
        }

        Cart existing = cartRepository.findByCustomerIdAndCropID(
                cart.getCustomer().getId(),
                cart.getCropID()
        );

        if (existing != null) {
            throw new IllegalArgumentException("Item already exists in cart");
        }

        // Save new item
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
    public CartGroupedResponseDTO getGroupedCartByCustomer(int customerId) {
        List<Cart> carts = cartRepository.findByCustomerId(customerId);
        if (carts == null) carts = new ArrayList<>();

        CartGroupedResponseDTO response = new CartGroupedResponseDTO();
        response.setGroupSellers(new ArrayList<>());
        response.setIndividualSellers(new ArrayList<>());

        Map<Boolean, List<Cart>> partitioned = carts.stream()
                .collect(Collectors.partitioningBy(cart -> cart.getGroupSellers() != null));

        partitioned.get(true).stream()
                .collect(Collectors.groupingBy(cart -> cart.getGroupSellers().getId()))
                .forEach((sellerId, groupCarts) -> {
                    GroupSellerCartDTO groupDto = new GroupSellerCartDTO();
                    groupDto.setId(sellerId);
                    groupDto.setGroupName(groupCarts.get(0).getGroupSellers().getGroupName());
                    groupDto.setCartItems(groupCarts.stream()
                            .map(this::convertToCartDTO)
                            .collect(Collectors.toList()));
                    response.getGroupSellers().add(groupDto);
                });

        // Process individual sellers
        partitioned.get(false).stream()
                .collect(Collectors.groupingBy(cart -> cart.getIndividualSellers().getId()))
                .forEach((sellerId, individualCarts) -> {
                    IndividualSellerCartDTO individualDto = new IndividualSellerCartDTO();
                    individualDto.setId(sellerId);
                    individualDto.setName(individualCarts.get(0).getIndividualSellers().getName());
                    individualDto.setCartItems(individualCarts.stream()
                            .map(this::convertToCartDTO)
                            .collect(Collectors.toList()));
                    response.getIndividualSellers().add(individualDto);
                });

        return response;
    }

    private CartDTO convertToCartDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setCropName(cart.getCropName());
        dto.setQuantity(cart.getQuantity());
        dto.setPrice(cart.getPrice());

        if (cart.getGroupSellers() != null) {
            dto.setSellerID(cart.getGroupSellers().getId());
            dto.setSellerName(cart.getGroupSellers().getGroupName());
            dto.setFirebaseID(cart.getGroupSellers().getFirebaseID());
        } else if (cart.getIndividualSellers() != null) {
            dto.setSellerID(cart.getIndividualSellers().getId());
            dto.setSellerName(cart.getIndividualSellers().getName());
            dto.setFirebaseID(cart.getIndividualSellers().getFirebaseID());
        }
        return dto;
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

    @Override
    public Cart findByCustomerAndCrop(int customerId, int cropId) {
        return cartRepository.findByCustomerIdAndCropID(customerId, cropId);
    }
}
