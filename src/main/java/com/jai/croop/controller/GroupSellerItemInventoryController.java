package com.jai.croop.controller;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.service.IGroupSellersItemInventoryService;
import com.jai.croop.service.IGroupSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-sellers-item-inventory")
public class GroupSellerItemInventoryController {
    @Autowired
    public IGroupSellersItemInventoryService groupSellersItemInventoryService;

    @Autowired
    public IGroupSellersService groupSellersService;

    //Add Items
    @PostMapping("/add-item")
    public ResponseEntity<GroupSellersItemInventory> addItem(@RequestBody GroupSellersItemInventory groupSellersItemInventory){
        System.out.println("Receive Group Seller Item: " + groupSellersItemInventory);
        int id = groupSellersItemInventory.getGroupSellers().getId();
        GroupSellers groupSellers = groupSellersService.getGroupSellers(id);
        return ResponseEntity.ok(groupSellersItemInventoryService.addItems(groupSellersItemInventory, groupSellers));
    }

    @GetMapping("/item/{itemName}/{id}")
    public ResponseEntity<List<GroupSellersItemInventory>> getItemByNameByIndividualID(@PathVariable String itemName,
                                                                                      @PathVariable int id){
        return ResponseEntity.ok(groupSellersItemInventoryService.findByItemNameAndGroupSellers_Id(itemName, id));
    }
    //Get Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<GroupSellersItemInventory> getItem(@PathVariable int id){
        return ResponseEntity.ok(groupSellersItemInventoryService.getItem(id));
    }

    //Get Item by Item Name
    @GetMapping("/items/{itemName}")
    public ResponseEntity<List<GroupSellersItemInventory>> getItemByName(@PathVariable String itemName){
        return ResponseEntity.ok(groupSellersItemInventoryService.findByItemName(itemName));
    }

    @GetMapping("/items/firebase/{firebaseID}")
    public ResponseEntity<List<GroupSellersItemInventory>> getItemsByFirebaseID(@PathVariable String firebaseID){
        return ResponseEntity.ok(groupSellersItemInventoryService.findByFirebaseID(firebaseID));
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
