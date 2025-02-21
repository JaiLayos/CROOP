package com.jai.croop.controller;

import com.jai.croop.model.DiscountDTO;
import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.service.IGroupSellersDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-seller-discount")
public class GroupSellerDiscountControlller {
    @Autowired
    private IGroupSellersDiscountService groupSellersDiscountService;
    @PostMapping
    public ResponseEntity<GroupSellerDiscount> addDiscount(@RequestBody GroupSellerDiscount groupSellerDiscount){
        GroupSellersProductsInventory groupSellersProductsInventory = groupSellerDiscount.getGroupSellersProductsInventory();
        GroupSellers groupSellers = groupSellerDiscount.getGroupSellers();
        System.out.println("Receive Group Seller Item: " + groupSellersProductsInventory);
        return ResponseEntity.ok(groupSellersDiscountService.addDiscounts(groupSellerDiscount, groupSellers, groupSellersProductsInventory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getDiscount(@PathVariable int id){
        GroupSellerDiscount groupSellerDiscount = groupSellersDiscountService.getDiscounts(id);
        DiscountDTO dto = new DiscountDTO();
        dto.setSellerName(groupSellerDiscount.getGroupSellers().getGroupName());
        dto.setItemName(groupSellerDiscount.getGroupSellersProductsInventory().getItemName());
        dto.setOriginalPrice(groupSellerDiscount.getOriginalPrice());
        dto.setDiscountPercent(groupSellerDiscount.getDiscountPercent());
        dto.setSalePrice(groupSellerDiscount.getSalePrice());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/discount/{itemName}")
    public ResponseEntity<List<GroupSellerDiscount>> getDiscountByName(@PathVariable String itemName){
        return ResponseEntity.ok(groupSellersDiscountService.findByItemName(itemName));
    }

    @GetMapping
    public ResponseEntity<List<GroupSellerDiscount>> getAllDiscounts(){
        return ResponseEntity.ok(groupSellersDiscountService.getAllDiscounts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupSellerDiscount> updateDiscount(@PathVariable int id, @RequestBody GroupSellerDiscount groupSellerDiscount){
        return ResponseEntity.ok((groupSellersDiscountService.updateDiscounts(id, groupSellerDiscount)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupSellerDiscount> deleteItem(@PathVariable int id){
        groupSellersDiscountService.deleteDiscounts(id);
        return ResponseEntity.noContent().build();
    }
}
