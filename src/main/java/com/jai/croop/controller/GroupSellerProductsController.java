package com.jai.croop.controller;

import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.service.IGroupSellersDiscountService;
import com.jai.croop.service.IGroupSellersProductInventoryService;
import com.jai.croop.service.IGroupSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/products/{itemName}")
    public ResponseEntity<List<GroupSellersProductsInventory>> getItemByName(@PathVariable String itemName){
        return ResponseEntity.ok(groupSellersProductsInventoryService.findByItemName(itemName));
    }

    @GetMapping("/items/firebase/{firebaseID}")
    public ResponseEntity<List<GroupSellersProductsInventory>> getItemsByFirebaseID(@PathVariable String firebaseID){
        return ResponseEntity.ok(groupSellersProductsInventoryService.findByFirebaseID(firebaseID));
    }

    @GetMapping
    public ResponseEntity<List<GroupSellersProductsInventory>> getAllItems(){
        return ResponseEntity.ok(groupSellersProductsInventoryService.getAllItems());
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
