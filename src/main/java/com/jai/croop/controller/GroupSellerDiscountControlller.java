package com.jai.croop.controller;

import com.jai.croop.model.DiscountDTO;
import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.service.IGroupSellersDiscountService;
import com.jai.croop.service.IGroupSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/group-seller-discount")
public class GroupSellerDiscountControlller {
    @Autowired
    private IGroupSellersDiscountService groupSellersDiscountService;

    @Autowired
    private IGroupSellersService groupSellersService;
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
        dto.setDiscountID(groupSellerDiscount.getId());
        dto.setSellerID(groupSellerDiscount.getGroupSellers().getId());
        dto.setProductID(groupSellerDiscount.getGroupSellersProductsInventory().getId());
        dto.setSellerName(groupSellerDiscount.getGroupSellers().getGroupName());
        dto.setItemName(groupSellerDiscount.getGroupSellersProductsInventory().getItemName());
        dto.setOriginalPrice(groupSellerDiscount.getOriginalPrice());
        dto.setDiscountPercent(groupSellerDiscount.getDiscountPercent());
        dto.setSalePrice(groupSellerDiscount.getSalePrice());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("firebase/{firebaseID}")
    public ResponseEntity<List<GroupSellerDiscount>> getDiscountbyFirebaseID(@PathVariable String firebaseID){
        int id = groupSellersService.getGroupSellerIdByFirebaseID(firebaseID);
        List<GroupSellerDiscount> groupSellerDiscount = groupSellersDiscountService.getDiscountsByGroupSellerID(id);
        return ResponseEntity.ok(groupSellerDiscount);
    }

    @GetMapping("/discount/{itemName}")
    public ResponseEntity<List<GroupSellerDiscount>> getDiscountByName(@PathVariable String itemName){
        return ResponseEntity.ok(groupSellersDiscountService.findByItemName(itemName));
    }

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts(){
        List<GroupSellerDiscount> groupSellerDiscounts = groupSellersDiscountService.getAllDiscounts();
        List<DiscountDTO> dtoList = new ArrayList<>();
        for(GroupSellerDiscount groupSellerDiscount : groupSellerDiscounts){
            DiscountDTO dto = new DiscountDTO();
            dto.setDiscountID(groupSellerDiscount.getId());
            dto.setSellerID(groupSellerDiscount.getGroupSellers().getId());
            dto.setSellerName(groupSellerDiscount.getGroupSellers().getGroupName());
            dto.setSellerRole(groupSellerDiscount.getGroupSellers().getRoles());
            dto.setFirebaseID(groupSellerDiscount.getGroupSellers().getFirebaseID());
            dto.setProductID(groupSellerDiscount.getGroupSellersProductsInventory().getId());
            dto.setItemName(groupSellerDiscount.getGroupSellersProductsInventory().getItemName());
            dto.setOriginalPrice(groupSellerDiscount.getOriginalPrice());
            dto.setDiscountPercent(groupSellerDiscount.getDiscountPercent());
            dto.setSalePrice(groupSellerDiscount.getSalePrice());
            dtoList.add(dto);
        }
        return ResponseEntity.ok(dtoList);
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
