package com.jai.croop.controller;

import com.jai.croop.model.*;
import com.jai.croop.service.IIndividualSellersItemService;
import com.jai.croop.service.IIndividualSellersProductService;
import com.jai.croop.service.IIndividualSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/individual-sellers-products-inventory")
public class IndividualSellerProductController {
    @Autowired
    public IIndividualSellersProductService individualSellersProductService;

    @Autowired
    public IIndividualSellersService individualSellersService;

    //Add Items
    @PostMapping("/add-item")
    public ResponseEntity<IndividualSellersProductsInventory> addItem(@RequestBody IndividualSellersProductsInventory individualSellersProductsInventory){
        System.out.println("Receive Individual Seller Product: " + individualSellersProductsInventory);
        int id = individualSellersProductsInventory.getIndividualSellers().getId();
        IndividualSellers individualSellers = individualSellersService.getIndividualSellers(id);
        return ResponseEntity.ok(individualSellersProductService.addItems(individualSellersProductsInventory, individualSellers));
    }

    @GetMapping("/in-season")
    public ResponseEntity<List<ProductDTO>> getInSeasonProducts(){
        List<IndividualSellersProductsInventory> individualSellersProductsInventories = individualSellersProductService.findTop3ByItemStart();
        List<ProductDTO> products = new ArrayList<>();
        for(IndividualSellersProductsInventory individualSellersProductsInventory:individualSellersProductsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(individualSellersProductsInventory.getId());
            productDTO.setProductName(individualSellersProductsInventory.getItemName());
            productDTO.setSellerID(individualSellersProductsInventory.getIndividualSellers().getId());
            productDTO.setProductSeller(individualSellersProductsInventory.getIndividualSellers().getName());
            productDTO.setSellerRole(individualSellersProductsInventory.getIndividualSellers().getRoles());
            productDTO.setFirebaseID(individualSellersProductsInventory.getIndividualSellers().getFirebaseID());
            productDTO.setProductPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getOriginalPrice());
            productDTO.setProductDiscount(individualSellersProductsInventory.getIndividualSellersDiscount().getDiscountPercent());
            productDTO.setProductFinalPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getSalePrice());
            products.add(productDTO);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/in-demand")
    public ResponseEntity<List<ProductDTO>> getInDemandProducts(){
        List<IndividualSellersProductsInventory> individualSellersProductsInventories = individualSellersProductService.findTop3ByLowestItemRemaining();
        List<ProductDTO> products = new ArrayList<>();
        for(IndividualSellersProductsInventory individualSellersProductsInventory:individualSellersProductsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(individualSellersProductsInventory.getId());
            productDTO.setProductName(individualSellersProductsInventory.getItemName());
            productDTO.setSellerID(individualSellersProductsInventory.getIndividualSellers().getId());
            productDTO.setProductSeller(individualSellersProductsInventory.getIndividualSellers().getName());
            productDTO.setSellerRole(individualSellersProductsInventory.getIndividualSellers().getRoles());
            productDTO.setFirebaseID(individualSellersProductsInventory.getIndividualSellers().getFirebaseID());
            productDTO.setProductPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getOriginalPrice());
            productDTO.setProductDiscount(individualSellersProductsInventory.getIndividualSellersDiscount().getDiscountPercent());
            productDTO.setProductFinalPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getSalePrice());
            products.add(productDTO);
        }
        return ResponseEntity.ok(products);
    }


    //Get Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<IndividualSellersProductsInventory> getItem(@PathVariable int id){
        return ResponseEntity.ok(individualSellersProductService.getItem(id));
    }

    @GetMapping("checkSC/{id}")
    public ResponseEntity<Boolean> checkIndividualSellerSC(@PathVariable int id){
        return ResponseEntity.ok(individualSellersProductService.findIfSCIsSet(id));
    }

    @GetMapping("checkMC/{id}")
    public ResponseEntity<Boolean> checkIndividualSellerMC(@PathVariable int id){
        return ResponseEntity.ok(individualSellersProductService.findIfMCIsSet(id));
    }

    @GetMapping("product/{id}")
    public ResponseEntity<ProductDTO> getIndividualProductDTO(@PathVariable int id){
        IndividualSellersProductsInventory individualSellersProductsInventory = individualSellersProductService.getItem(id);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductID(individualSellersProductsInventory.getId());
        productDTO.setProductName(individualSellersProductsInventory.getItemName());
        productDTO.setSellerID(individualSellersProductsInventory.getIndividualSellers().getId());
        productDTO.setProductSeller(individualSellersProductsInventory.getIndividualSellers().getName());
        productDTO.setSellerRole(individualSellersProductsInventory.getIndividualSellers().getRoles());
        productDTO.setFirebaseID(individualSellersProductsInventory.getIndividualSellers().getFirebaseID());
        productDTO.setProductPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getOriginalPrice());
        productDTO.setProductDiscount(individualSellersProductsInventory.getIndividualSellersDiscount().getDiscountPercent());
        productDTO.setProductFinalPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getSalePrice());
        return ResponseEntity.ok(productDTO);
    }

    //Get Item by Item Name
    @GetMapping("/items/{itemName}")
    public ResponseEntity<List<IndividualSellersProductsInventory>> getItemByName(@PathVariable String itemName){
        return ResponseEntity.ok(individualSellersProductService.findByItemName(itemName));
    }

    @GetMapping("/productDTO/{itemName}")
    public ResponseEntity<List<ProductDTO>> getProductDTOByName(@PathVariable String itemName){
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<IndividualSellersProductsInventory> individualSellersProductsInventories = individualSellersProductService.findByItemName(itemName);
        for(IndividualSellersProductsInventory individualSellersProductsInventory : individualSellersProductsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(individualSellersProductsInventory.getId());
            productDTO.setProductName(individualSellersProductsInventory.getItemName());
            productDTO.setSellerID(individualSellersProductsInventory.getIndividualSellers().getId());
            productDTO.setProductSeller(individualSellersProductsInventory.getIndividualSellers().getName());
            productDTO.setFirebaseID(individualSellersProductsInventory.getIndividualSellers().getFirebaseID());
            productDTO.setProductPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getOriginalPrice());
            productDTO.setProductDiscount(individualSellersProductsInventory.getIndividualSellersDiscount().getDiscountPercent());
            productDTO.setProductFinalPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getSalePrice());
            productDTOS.add(productDTO);
        }
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/items/firebase/{firebaseID}")
    public ResponseEntity<List<IndividualSellersProductsInventory>> getItemsByFirebaseID(@PathVariable String firebaseID){
        return ResponseEntity.ok(individualSellersProductService.findByFirebaseID(firebaseID));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllItems(){
        List<IndividualSellersProductsInventory> individualSellersProductsInventories = individualSellersProductService.getAllItems();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(IndividualSellersProductsInventory individualSellersProductsInventory : individualSellersProductsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(individualSellersProductsInventory.getId());
            productDTO.setProductName(individualSellersProductsInventory.getItemName());
            productDTO.setSellerID(individualSellersProductsInventory.getIndividualSellers().getId());
            productDTO.setProductPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getOriginalPrice());
            productDTO.setProductDiscount(individualSellersProductsInventory.getIndividualSellersDiscount().getDiscountPercent());
            productDTO.setProductFinalPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getSalePrice());
            productDTO.setProductSeller(individualSellersProductsInventory.getIndividualSellers().getName());
            productDTO.setSellerRole(individualSellersProductsInventory.getIndividualSellers().getRoles());
            productDTO.setFirebaseID(individualSellersProductsInventory.getIndividualSellers().getFirebaseID());
            productDTO.setRemaining(individualSellersProductsInventory.getItemRemaining());
            productDTOS.add(productDTO);
        }
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/products/firebase/{firebaseID}")
    public ResponseEntity<List<ProductDTO>> getAllProductDTOsByFirebaseID(@PathVariable String firebaseID){
        List<IndividualSellersProductsInventory> individualSellersProductsInventories = individualSellersProductService.findByFirebaseID(firebaseID);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(IndividualSellersProductsInventory individualSellersProductsInventory : individualSellersProductsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(individualSellersProductsInventory.getId());
            productDTO.setProductName(individualSellersProductsInventory.getItemName());
            productDTO.setProductPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getOriginalPrice());
            productDTO.setProductDiscount(individualSellersProductsInventory.getIndividualSellersDiscount().getDiscountPercent());
            productDTO.setProductFinalPrice(individualSellersProductsInventory.getIndividualSellersDiscount().getSalePrice());
            productDTO.setSellerID(individualSellersProductsInventory.getIndividualSellers().getId());
            productDTO.setProductSeller(individualSellersProductsInventory.getIndividualSellers().getName());
            productDTO.setSellerRole(individualSellersProductsInventory.getIndividualSellers().getRoles());
            productDTO.setFirebaseID(individualSellersProductsInventory.getIndividualSellers().getFirebaseID());
            productDTO.setRemaining(individualSellersProductsInventory.getItemRemaining());
            productDTOS.add(productDTO);
        }
        return ResponseEntity.ok(productDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndividualSellersProductsInventory> updateItem(@PathVariable int id, @RequestBody IndividualSellersProductsInventory individualSellersProductsInventory){
        return ResponseEntity.ok((individualSellersProductService.updateItems(id, individualSellersProductsInventory)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IndividualSellersProductsInventory> deleteItem(@PathVariable int id){
        individualSellersProductService.deleteItems(id);
        return ResponseEntity.noContent().build();
    }
}
