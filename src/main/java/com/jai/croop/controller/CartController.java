package com.jai.croop.controller;

import com.jai.croop.model.*;
import com.jai.croop.service.ICartService;
import com.jai.croop.service.ICustomerService;
import com.jai.croop.service.IIndividualSellersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private ICartService cartService;
    @Autowired
    private IIndividualSellersService individualSellersService;
    @Autowired
    private ICustomerService customerService;


    @PostMapping
    public ResponseEntity<?> addCart(@RequestBody Cart cart) {
        try {
            Cart savedCart = cartService.addCart(cart);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/grouped/{customerId}")
    public ResponseEntity<CartGroupedResponseDTO> getGroupedCart(
            @PathVariable int customerId) {
        CartGroupedResponseDTO response = cartService.getGroupedCartByCustomer(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/from-group")
    public ResponseEntity<List<CartDTO>> getAllCartFromGroup() {
        List<Cart> carts = cartService.getAllCart();
        List<CartDTO> cartDTOS = new ArrayList<>();
        for(Cart cart : carts){
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setCropName(cart.getCropName());
            cartDTO.setQuantity(cart.getQuantity());
            cartDTO.setPrice(cart.getPrice());
            cartDTO.setCustomerID(cart.getCustomer().getId());
            cartDTO.setCustomerName(cart.getCustomer().getName());
            cartDTO.setSellerID(cart.getGroupSellers().getId());
            cartDTO.setSellerName(cart.getGroupSellers().getGroupName());
            cartDTO.setFirebaseID(cart.getGroupSellers().getFirebaseID());
            cartDTOS.add(cartDTO);
        }
        return ResponseEntity.ok(cartDTOS);
    }

    @GetMapping("/from-individual")
    public ResponseEntity<List<CartDTO>> getAllCartFromIndividual() {
        List<Cart> carts = cartService.getAllCart();
        List<CartDTO> cartDTOS = new ArrayList<>();
        for(Cart cart : carts){
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setCropName(cart.getCropName());
            cartDTO.setQuantity(cart.getQuantity());
            cartDTO.setPrice(cart.getPrice());
            cartDTO.setCustomerID(cart.getCustomer().getId());
            cartDTO.setCustomerName(cart.getCustomer().getName());
            cartDTO.setSellerID(cart.getIndividualSellers().getId());
            cartDTO.setSellerName(cart.getIndividualSellers().getName());
            cartDTO.setFirebaseID(cart.getIndividualSellers().getFirebaseID());
            cartDTOS.add(cartDTO);
        }
        return ResponseEntity.ok(cartDTOS);
    }

    @GetMapping("/from-group/{id}")
    public ResponseEntity<CartDTO> getCartFromGroup(@PathVariable int id) {
        Cart cart = cartService.getCart(id);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCropName(cart.getCropName());
        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setPrice(cart.getPrice());
        cartDTO.setSellerID(cart.getGroupSellers().getId());
        cartDTO.setSellerName(cart.getGroupSellers().getGroupName());
        cartDTO.setFirebaseID(cart.getGroupSellers().getFirebaseID());
        cartDTO.setCustomerID(cart.getCustomer().getId());
        cartDTO.setCustomerName(cart.getCustomer().getName());
        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("/from-individual/{id}")
    public ResponseEntity<CartDTO> getCartFromIndividual(@PathVariable int id) {
        Cart cart = cartService.getCart(id);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCropName(cart.getCropName());
        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setPrice(cart.getPrice());
        cartDTO.setSellerID(cart.getIndividualSellers().getId());
        cartDTO.setSellerName(cart.getIndividualSellers().getName());
        cartDTO.setFirebaseID(cart.getIndividualSellers().getFirebaseID());
        cartDTO.setCustomerID(cart.getCustomer().getId());
        cartDTO.setCustomerName(cart.getCustomer().getName());
        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("from-group/seller/{id}")
    public ResponseEntity<List<CartDTO>> getGroupCartBySellerID(@PathVariable int id) {
        List<Cart> carts = cartService.getGroupCartBySellerID(id);
        List<CartDTO> cartDTOS = new ArrayList<>();
        for(Cart cart : carts){
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setCropName(cart.getCropName());
            cartDTO.setQuantity(cart.getQuantity());
            cartDTO.setPrice(cart.getPrice());
            cartDTO.setCustomerID(cart.getCustomer().getId());
            cartDTO.setCustomerName(cart.getCustomer().getName());
            cartDTO.setSellerID(cart.getGroupSellers().getId());
            cartDTO.setSellerName(cart.getGroupSellers().getGroupName());
            cartDTO.setFirebaseID(cart.getGroupSellers().getFirebaseID());
            cartDTOS.add(cartDTO);
        }
        return ResponseEntity.ok(cartDTOS);
    }

    @GetMapping("from-individual/seller/{id}")
    public ResponseEntity<List<CartDTO>> getIndividualCartBySellerID(@PathVariable int id) {
        List<Cart> carts = cartService.getIndividualCartBySellerID(id);
        List<CartDTO> cartDTOS = new ArrayList<>();
        for(Cart cart : carts){
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setCropName(cart.getCropName());
            cartDTO.setQuantity(cart.getQuantity());
            cartDTO.setPrice(cart.getPrice());
            cartDTO.setCustomerID(cart.getCustomer().getId());
            cartDTO.setCustomerName(cart.getCustomer().getName());
            cartDTO.setSellerID(cart.getIndividualSellers().getId());
            cartDTO.setSellerName(cart.getIndividualSellers().getName());
            cartDTO.setFirebaseID(cart.getIndividualSellers().getFirebaseID());
            cartDTOS.add(cartDTO);
        }
        return ResponseEntity.ok(cartDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable int id, @RequestBody CartDTO cartDTO) {
        // Fetch the existing cart by ID
        Cart current = cartService.getCart(id);

        if (current == null) {
            return ResponseEntity.notFound().build(); // Return 404 if the cart item doesn't exist
        }

        Cart updatedCart = convertToCart(cartDTO);

        updatedCart.setId(current.getId());
        IndividualSellers individualSeller =  individualSellersService.getIndividualSellers(cartDTO.getSellerID());
        updatedCart.setIndividualSellers(individualSeller);
        Customer customer = customerService.getCustomer(cartDTO.getCustomerID());
        updatedCart.setCustomer(customer);

        Cart savedCart = cartService.updateCart(id, updatedCart);
        CartDTO updatedCartDTO = convertToCartDTO(savedCart);

        return ResponseEntity.ok(updatedCartDTO); // Return the updated CartDTO
    }

    private Cart convertToCart(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setCropName(cartDTO.getCropName());
        cart.setQuantity(cartDTO.getQuantity());
        cart.setPrice(cartDTO.getPrice());

        // Relationships are handled separately in the controller
        return cart;
    }

    private CartDTO convertToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCropName(cart.getCropName());
        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setPrice(cart.getPrice());

        if (cart.getCustomer() != null) {
            cartDTO.setCustomerID(cart.getCustomer().getId());
            cartDTO.setCustomerName(cart.getCustomer().getName());
        }

        if (cart.getIndividualSellers() != null) {
            cartDTO.setSellerID(cart.getIndividualSellers().getId());
            cartDTO.setSellerName(cart.getIndividualSellers().getName());
            cartDTO.setFirebaseID(cart.getIndividualSellers().getFirebaseID());
        }

        return cartDTO;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> deleteCart(@PathVariable int id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
    
}
