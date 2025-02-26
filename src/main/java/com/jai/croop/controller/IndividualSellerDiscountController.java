package com.jai.croop.controller;

import com.jai.croop.model.*;
import com.jai.croop.service.IGroupSellersDiscountService;
import com.jai.croop.service.IGroupSellersService;
import com.jai.croop.service.IIndividualSellersDiscountService;
import com.jai.croop.service.IIndividualSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/individual-seller-discount")
public class IndividualSellerDiscountController {
    @Autowired
    private IIndividualSellersDiscountService individualSellersDiscountService;

    @Autowired
    private IIndividualSellersService individualSellersService;
    @PostMapping
    public ResponseEntity<IndividualSellersDiscount> addDiscount(@RequestBody IndividualSellersDiscount individualSellersDiscount){
        IndividualSellersProductsInventory individualSellersProductsInventory = individualSellersDiscount.getIndividualSellersProductsInventory();
        IndividualSellers individualSellers = individualSellersDiscount.getIndividualSellers();
        System.out.println("Receive Individual Seller Product: " + individualSellersProductsInventory);
        return ResponseEntity.ok(individualSellersDiscountService.addDiscounts(individualSellersDiscount, individualSellers, individualSellersProductsInventory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getDiscount(@PathVariable int id){
        IndividualSellersDiscount individualSellersDiscount = individualSellersDiscountService.getDiscounts(id);
        DiscountDTO dto = new DiscountDTO();
        dto.setDiscountID(individualSellersDiscount.getId());
        dto.setSellerID(individualSellersDiscount.getIndividualSellers().getId());
        dto.setProductID(individualSellersDiscount.getIndividualSellersProductsInventory().getId());
        dto.setSellerName(individualSellersDiscount.getIndividualSellers().getName());
        dto.setItemName(individualSellersDiscount.getIndividualSellersProductsInventory().getItemName());
        dto.setOriginalPrice(individualSellersDiscount.getOriginalPrice());
        dto.setDiscountPercent(individualSellersDiscount.getDiscountPercent());
        dto.setSalePrice(individualSellersDiscount.getSalePrice());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("firebase/{firebaseID}")
    public ResponseEntity<List<IndividualSellersDiscount>> getDiscountbyFirebaseID(@PathVariable String firebaseID){
        int id = individualSellersService.getIndividualSellerIdByFirebaseID(firebaseID);
        List<IndividualSellersDiscount> individualSellersDiscounts = individualSellersDiscountService.getDiscountsByIndividualSellerID(id);
        return ResponseEntity.ok(individualSellersDiscounts);
    }

    @GetMapping("/discount/{itemName}")
    public ResponseEntity<List<IndividualSellersDiscount>> getDiscountByName(@PathVariable String itemName){
        return ResponseEntity.ok(individualSellersDiscountService.findByItemName(itemName));
    }

    @GetMapping
    public ResponseEntity<List<IndividualSellersDiscount>> getAllDiscounts(){
        return ResponseEntity.ok(individualSellersDiscountService.getAllDiscounts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndividualSellersDiscount> updateDiscount(@PathVariable int id, @RequestBody IndividualSellersDiscount individualSellersDiscount){
        return ResponseEntity.ok((individualSellersDiscountService.updateDiscounts(id, individualSellersDiscount)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IndividualSellersDiscount> deleteItem(@PathVariable int id){
        individualSellersDiscountService.deleteDiscounts(id);
        return ResponseEntity.noContent().build();
    }
}
