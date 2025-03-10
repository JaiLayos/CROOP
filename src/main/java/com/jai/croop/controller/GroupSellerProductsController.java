package com.jai.croop.controller;

import com.jai.croop.model.*;
import com.jai.croop.service.IGroupSellersDiscountService;
import com.jai.croop.service.IGroupSellersProductInventoryService;
import com.jai.croop.service.IGroupSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/group-sellers-products")
public class GroupSellerProductsController {
    @Autowired
    public IGroupSellersProductInventoryService groupSellersProductsInventoryService;

    @Autowired
    public IGroupSellersDiscountService groupSellersDiscountService;

    @Autowired
    public IGroupSellersService groupSellersService;

    @PostMapping
    public ResponseEntity<GroupSellersProductsInventory> addItem(@RequestBody GroupSellersProductsInventory groupSellersProductsInventory){
        GroupSellers groupSellers = groupSellersService.getGroupSellers(groupSellersProductsInventory.getGroupSellers().getId());
        System.out.println("Receive Group Seller Item: " + groupSellersProductsInventory);
        return ResponseEntity.ok(groupSellersProductsInventoryService.addItems(groupSellersProductsInventory, groupSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupSellersProductsInventory> getItem(@PathVariable int id){
        return ResponseEntity.ok(groupSellersProductsInventoryService.getItem(id));
    }

    @GetMapping("product/{id}")
    public ResponseEntity<ProductDTO> getGroupProductDTO(@PathVariable int id){
        GroupSellersProductsInventory groupSellersProductsInventory = groupSellersProductsInventoryService.getItem(id);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductID(groupSellersProductsInventory.getId());
        productDTO.setProductName(groupSellersProductsInventory.getItemName());
        productDTO.setSellerID(groupSellersProductsInventory.getGroupSellers().getId());
        productDTO.setProductPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getOriginalPrice());
        productDTO.setProductDiscount(groupSellersProductsInventory.getGroupSellerDiscounts().getDiscountPercent());
        productDTO.setProductFinalPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getSalePrice());
        productDTO.setProductSeller(groupSellersProductsInventory.getGroupSellers().getGroupName());
        productDTO.setSellerRole(groupSellersProductsInventory.getGroupSellers().getRoles());
        productDTO.setFirebaseID(groupSellersProductsInventory.getGroupSellers().getFirebaseID());
        productDTO.setRemaining(groupSellersProductsInventory.getItemRemaining());
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/products/{itemName}")
    public ResponseEntity<List<GroupSellersProductsInventory>> getItemByName(@PathVariable String itemName){
        return ResponseEntity.ok(groupSellersProductsInventoryService.findByItemName(itemName));
    }

    @GetMapping("/products/{itemName}/{id}")
    public ResponseEntity<List<GroupSellersProductsInventory>> getItemByNameByGroupID(@PathVariable String itemName,
                                                                                      @PathVariable int id){
        return ResponseEntity.ok(groupSellersProductsInventoryService.findByItemNameAndGroupSellers_Id(itemName, id));
    }

    @GetMapping("/productDTO/{itemName}")
    public ResponseEntity<List<ProductDTO>> getProductDTOByName(@PathVariable String itemName){
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<GroupSellersProductsInventory> productsInventories = groupSellersProductsInventoryService.findByItemName(itemName);
        for(GroupSellersProductsInventory groupSellersProductsInventory : productsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(groupSellersProductsInventory.getId());
            productDTO.setProductName(groupSellersProductsInventory.getItemName());
            productDTO.setSellerID(groupSellersProductsInventory.getGroupSellers().getId());
            productDTO.setProductPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getOriginalPrice());
            productDTO.setProductDiscount(groupSellersProductsInventory.getGroupSellerDiscounts().getDiscountPercent());
            productDTO.setProductFinalPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getSalePrice());
            productDTO.setProductSeller(groupSellersProductsInventory.getGroupSellers().getGroupName());
            productDTO.setSellerRole(groupSellersProductsInventory.getGroupSellers().getRoles());
            productDTO.setFirebaseID(groupSellersProductsInventory.getGroupSellers().getFirebaseID());
            productDTO.setRemaining(groupSellersProductsInventory.getItemRemaining());
            productDTOS.add(productDTO);
        }
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/items/firebase/{firebaseID}")
    public ResponseEntity<List<GroupSellersProductsInventory>> getItemsByFirebaseID(@PathVariable String firebaseID){
        return ResponseEntity.ok(groupSellersProductsInventoryService.findByFirebaseID(firebaseID));
    }

    @GetMapping("/productsDTO/firebase/{firebaseID}")
    public ResponseEntity<List<ProductDTO>> getProductsDTOByFirebaseID(@PathVariable String firebaseID){
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<GroupSellersProductsInventory> productsInventories = groupSellersProductsInventoryService.findByFirebaseID(firebaseID);
        for(GroupSellersProductsInventory groupSellersProductsInventory : productsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(groupSellersProductsInventory.getId());
            productDTO.setProductName(groupSellersProductsInventory.getItemName());
            productDTO.setProductPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getOriginalPrice());
            productDTO.setProductDiscount(groupSellersProductsInventory.getGroupSellerDiscounts().getDiscountPercent());
            productDTO.setProductFinalPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getSalePrice());
            productDTO.setSellerID(groupSellersProductsInventory.getGroupSellers().getId());
            productDTO.setProductSeller(groupSellersProductsInventory.getGroupSellers().getGroupName());
            productDTO.setSellerRole(groupSellersProductsInventory.getGroupSellers().getRoles());
            productDTO.setFirebaseID(groupSellersProductsInventory.getGroupSellers().getFirebaseID());
            productDTO.setRemaining(groupSellersProductsInventory.getItemRemaining());
            productDTOS.add(productDTO);
        }
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllItems(){
        List<GroupSellersProductsInventory> groupSellersProductsInventories = groupSellersProductsInventoryService.getAllItems();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for(GroupSellersProductsInventory groupSellersProductsInventory: groupSellersProductsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(groupSellersProductsInventory.getId());
            productDTO.setProductName(groupSellersProductsInventory.getItemName());
            productDTO.setProductPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getOriginalPrice());
            productDTO.setProductDiscount(groupSellersProductsInventory.getGroupSellerDiscounts().getDiscountPercent());
            productDTO.setProductFinalPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getSalePrice());
            productDTO.setSellerID(groupSellersProductsInventory.getGroupSellers().getId());
            productDTO.setProductSeller(groupSellersProductsInventory.getGroupSellers().getGroupName());
            productDTO.setSellerRole(groupSellersProductsInventory.getGroupSellers().getRoles());
            productDTO.setFirebaseID(groupSellersProductsInventory.getGroupSellers().getFirebaseID());
            productDTO.setRemaining(groupSellersProductsInventory.getItemRemaining());
            productDTOs.add(productDTO);
        }
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/in-season")
    public ResponseEntity<List<ProductDTO>> getInSeasonProducts(){
        List<GroupSellersProductsInventory> groupSellersProductsInventories = groupSellersProductsInventoryService.getTop3ProductsByItemStart();
        List<ProductDTO> products = new ArrayList<>();
           for(GroupSellersProductsInventory groupSellersProductsInventory: groupSellersProductsInventories){
               ProductDTO productDTO = new ProductDTO();
               productDTO.setProductID(groupSellersProductsInventory.getId());
               productDTO.setProductName(groupSellersProductsInventory.getItemName());
               productDTO.setSellerID(groupSellersProductsInventory.getGroupSellers().getId());
               productDTO.setProductSeller(groupSellersProductsInventory.getGroupSellers().getGroupName());
               productDTO.setSellerRole(groupSellersProductsInventory.getGroupSellers().getRoles());
               productDTO.setFirebaseID(groupSellersProductsInventory.getGroupSellers().getFirebaseID());
               productDTO.setProductPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getOriginalPrice());
               productDTO.setProductDiscount(groupSellersProductsInventory.getGroupSellerDiscounts().getDiscountPercent());
               productDTO.setProductFinalPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getSalePrice());
               products.add(productDTO);
           }
       return ResponseEntity.ok(products);
    }

    @GetMapping("/in-demand")
    public ResponseEntity<List<ProductDTO>> getInDemandProducts(){
        List<GroupSellersProductsInventory> groupSellersProductsInventories = groupSellersProductsInventoryService.findTop3ByLowestItemRemaining();
        List<ProductDTO> products = new ArrayList<>();
        for(GroupSellersProductsInventory groupSellersProductsInventory: groupSellersProductsInventories){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(groupSellersProductsInventory.getId());
            productDTO.setProductName(groupSellersProductsInventory.getItemName());
            productDTO.setSellerID(groupSellersProductsInventory.getGroupSellers().getId());
            productDTO.setProductSeller(groupSellersProductsInventory.getGroupSellers().getGroupName());
            productDTO.setSellerRole(groupSellersProductsInventory.getGroupSellers().getRoles());
            productDTO.setFirebaseID(groupSellersProductsInventory.getGroupSellers().getFirebaseID());
            productDTO.setProductPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getOriginalPrice());
            productDTO.setProductDiscount(groupSellersProductsInventory.getGroupSellerDiscounts().getDiscountPercent());
            productDTO.setProductFinalPrice(groupSellersProductsInventory.getGroupSellerDiscounts().getSalePrice());
            products.add(productDTO);
        }
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupSellersProductsInventory> updateItem(@PathVariable int id, @RequestBody GroupSellersProductsInventory groupSellersProductsInventory){
        return ResponseEntity.ok((groupSellersProductsInventoryService.updateItems(id, groupSellersProductsInventory)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupSellersProductsInventory> deleteItem(@PathVariable int id){
        groupSellersProductsInventoryService.deleteItems(id);
        return ResponseEntity.noContent().build();
    }
}
