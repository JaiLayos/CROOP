package com.jai.croop.controller;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersOrders;
import com.jai.croop.service.IGroupSellersItemInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-sellers-item-inventory")
public class GroupSellerItemInventoryController {
    @Autowired
    public IGroupSellersItemInventoryService groupSellersItemInventoryService;

    @PostMapping
    public ResponseEntity<GroupSellersItemInventory> addItem(@RequestBody GroupSellersItemInventory groupSellersItemInventory, GroupSellers groupSellers){
        System.out.println("Receive Group Seller Item: " + groupSellersItemInventory);
        return ResponseEntity.ok(groupSellersItemInventoryService.addItems(groupSellersItemInventory, groupSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupSellersItemInventory> getItem(@PathVariable int id){
        return ResponseEntity.ok(groupSellersItemInventoryService.getItem(id));
    }

    @GetMapping("/items/{itemName}")
    public ResponseEntity<List<GroupSellersItemInventory>> getItemByName(@PathVariable String itemName){
        return ResponseEntity.ok(groupSellersItemInventoryService.findByItemName(itemName));
    }

    @GetMapping
    public ResponseEntity<List<GroupSellersItemInventory>> getAllItems(){
        return ResponseEntity.ok(groupSellersItemInventoryService.getAllItems());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupSellersItemInventory> updateItem(@PathVariable int id, @RequestBody GroupSellersItemInventory groupSellersItemInventory){
        return ResponseEntity.ok((groupSellersItemInventoryService.updateItems(id, groupSellersItemInventory)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupSellersItemInventory> deleteItem(@PathVariable int id){
        groupSellersItemInventoryService.deleteItems(id);
        return ResponseEntity.noContent().build();
    }
}
