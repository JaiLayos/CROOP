package com.jai.croop.controller;

import com.jai.croop.model.IndividualSellers;
import com.jai.croop.model.IndividualSellersItemInventory;
import com.jai.croop.model.IndividualSellersProductsInventory;
import com.jai.croop.service.IIndividualSellersItemService;
import com.jai.croop.service.IIndividualSellersProductService;
import com.jai.croop.service.IIndividualSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //Get Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<IndividualSellersProductsInventory> getItem(@PathVariable int id){
        return ResponseEntity.ok(individualSellersProductService.getItem(id));
    }

    //Get Item by Item Name
    @GetMapping("/items/{itemName}")
    public ResponseEntity<List<IndividualSellersProductsInventory>> getItemByName(@PathVariable String itemName){
        return ResponseEntity.ok(individualSellersProductService.findByItemName(itemName));
    }

    @GetMapping("/items/firebase/{firebaseID}")
    public ResponseEntity<List<IndividualSellersProductsInventory>> getItemsByFirebaseID(@PathVariable String firebaseID){
        return ResponseEntity.ok(individualSellersProductService.findByFirebaseID(firebaseID));
    }

    @GetMapping
    public ResponseEntity<List<IndividualSellersProductsInventory>> getAllItems(){
        return ResponseEntity.ok(individualSellersProductService.getAllItems());
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
