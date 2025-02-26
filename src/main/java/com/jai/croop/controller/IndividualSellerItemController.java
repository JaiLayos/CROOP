package com.jai.croop.controller;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.IndividualSellers;
import com.jai.croop.model.IndividualSellersItemInventory;
import com.jai.croop.service.IGroupSellersItemInventoryService;
import com.jai.croop.service.IGroupSellersService;
import com.jai.croop.service.IIndividualSellersItemService;
import com.jai.croop.service.IIndividualSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/individual-sellers-item-inventory")
public class IndividualSellerItemController {
    @Autowired
    public IIndividualSellersItemService individualSellersItemService;

    @Autowired
    public IIndividualSellersService individualSellersService;

    //Add Items
    @PostMapping("/add-item")
    public ResponseEntity<IndividualSellersItemInventory> addItem(@RequestBody IndividualSellersItemInventory individualSellersItemInventory){
        System.out.println("Receive Individual Seller Item: " + individualSellersItemInventory);
        int id = individualSellersItemInventory.getIndividualSellers().getId();
        IndividualSellers individualSellers = individualSellersService.getIndividualSellers(id);
        return ResponseEntity.ok(individualSellersItemService.addItems(individualSellersItemInventory, individualSellers));
    }

    //Get Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<IndividualSellersItemInventory> getItem(@PathVariable int id){
        return ResponseEntity.ok(individualSellersItemService.getItem(id));
    }

    //Get Item by Item Name
    @GetMapping("/items/{itemName}")
    public ResponseEntity<List<IndividualSellersItemInventory>> getItemByName(@PathVariable String itemName){
        return ResponseEntity.ok(individualSellersItemService.findByItemName(itemName));
    }

    @GetMapping("/items/firebase/{firebaseID}")
    public ResponseEntity<List<IndividualSellersItemInventory>> getItemsByFirebaseID(@PathVariable String firebaseID){
        return ResponseEntity.ok(individualSellersItemService.findByFirebaseID(firebaseID));
    }

    @GetMapping
    public ResponseEntity<List<IndividualSellersItemInventory>> getAllItems(){
        return ResponseEntity.ok(individualSellersItemService.getAllItems());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndividualSellersItemInventory> updateItem(@PathVariable int id, @RequestBody IndividualSellersItemInventory individualSellersItemInventory){
        return ResponseEntity.ok((individualSellersItemService.updateItems(id, individualSellersItemInventory)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IndividualSellersItemInventory> deleteItem(@PathVariable int id){
        individualSellersItemService.deleteItems(id);
        return ResponseEntity.noContent().build();
    }
}
