package com.jai.croop.controller;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.IndividualSellers;
import com.jai.croop.model.IndividualSellersOrders;
import com.jai.croop.model.SellerOrdersDTO;
import com.jai.croop.service.GroupSellersOrdersService;
import com.jai.croop.service.IGroupSellersService;
import com.jai.croop.service.IIndividualSellersService;
import com.jai.croop.service.IndividualSellersOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/individual-sellers")
public class IndividualSellerController {
    @Autowired
    public IIndividualSellersService individualSellersService;

    @Autowired
    public IndividualSellersOrdersService individualSellersOrdersService;

    @PostMapping
    public ResponseEntity<IndividualSellers> addIndividualSellers(@RequestBody IndividualSellers individualSellers){
        System.out.println("Receive Seller: " + individualSellers);
        return ResponseEntity.ok(individualSellersService.addIndividualSellers(individualSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndividualSellers> getIndividualSellers(@PathVariable int id){
        return ResponseEntity.ok(individualSellersService.getIndividualSellers(id));
    }

    @GetMapping("/id/{firebaseID}")
    public ResponseEntity<Integer> getIndividualSellerID(@PathVariable String firebaseID){
        return ResponseEntity.ok(individualSellersService.findIDByFirebaseID(firebaseID));
    }

    @GetMapping("/orders/{firebaseID}")
    public ResponseEntity<List<SellerOrdersDTO>> getIndividualSellersbyFirebase(@PathVariable String firebaseID){
        int id = individualSellersService.getIndividualSellerIdByFirebaseID(firebaseID);
        return ResponseEntity.ok(individualSellersOrdersService.findByIndividualSellerID(id));
    }

    @GetMapping("/details/{firebaseID}")
    public ResponseEntity<IndividualSellers> getIndividualSellersbyFirebaseID(@PathVariable String firebaseID){
        int id = individualSellersService.getIndividualSellerIdByFirebaseID(firebaseID);
        return ResponseEntity.ok(individualSellersService.getIndividualSellers(id));
    }

    @GetMapping
    public ResponseEntity<List<IndividualSellers>> getAllIndividualSellers(){
        return ResponseEntity.ok(individualSellersService.getAllIndividualSellers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndividualSellers> updateIndividualSellers(@PathVariable int id, @RequestBody IndividualSellers individualSellers){
        return ResponseEntity.ok((individualSellersService.updateIndividualSellers(id, individualSellers)));
    }

    @PutMapping("/firebase/{firebaseID}")
    public ResponseEntity<IndividualSellers> updateIndividualSellersByFirebaseID(@PathVariable String firebaseID, @RequestBody IndividualSellers individualSellers){
        return ResponseEntity.ok((individualSellersService.updateIndividualSellersByFirebaseID(firebaseID, individualSellers)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IndividualSellers> deleteIndividualSellers(@PathVariable int id){
        individualSellersService.deleteIndividualSellers(id);
        return ResponseEntity.noContent().build();
    }
}
