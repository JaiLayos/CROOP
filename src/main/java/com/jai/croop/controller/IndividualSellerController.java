package com.jai.croop.controller;

import com.jai.croop.model.*;
import com.jai.croop.service.IIndividualSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/individual-sellers")
public class IndividualSellerController {
    @Autowired
    public IIndividualSellersService individualSellersService;


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
    @GetMapping("/featured")
    public ResponseEntity<List<FeaturedSellersDTO>> getFeaturedIndividual(){
        List<IndividualSellers> individualSellers = individualSellersService.findTop3ByTotalItemStart();
        List<FeaturedSellersDTO> featured = new ArrayList<>();
        for(IndividualSellers individualSeller: individualSellers){
            FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
            featuredSellersDTO.setId(individualSeller.getId());
            featuredSellersDTO.setName(individualSeller.getName());
            featuredSellersDTO.setRole(individualSeller.getRoles());
            featuredSellersDTO.setFirebaseID(individualSeller.getFirebaseID());
            featured.add(featuredSellersDTO);
        }
        return ResponseEntity.ok(featured);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<FeaturedSellersDTO>> getAllIndividualSellersDTO(){
        List<IndividualSellers> individualSellers = individualSellersService.getAllIndividualSellers();
        List<FeaturedSellersDTO> featured = new ArrayList<>();
        for(IndividualSellers individualSeller: individualSellers){
            FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
            featuredSellersDTO.setId(individualSeller.getId());
            featuredSellersDTO.setName(individualSeller.getName());
            featuredSellersDTO.setRole(individualSeller.getRoles());
            featuredSellersDTO.setFirebaseID(individualSeller.getFirebaseID());
            featured.add(featuredSellersDTO);
        }
        return ResponseEntity.ok(featured);
    }

    @GetMapping("individual-seller/{name}")
    public ResponseEntity<List<FeaturedSellersDTO>> findIndividualSellerByName(String name){
        List<IndividualSellers> individualSellers = individualSellersService.findByName(name);
        List<FeaturedSellersDTO> featured = new ArrayList<>();
        for(IndividualSellers individualSeller: individualSellers){
            FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
            featuredSellersDTO.setId(individualSeller.getId());
            featuredSellersDTO.setName(individualSeller.getName());
            featuredSellersDTO.setRole(individualSeller.getRoles());
            featuredSellersDTO.setFirebaseID(individualSeller.getFirebaseID());
            featured.add(featuredSellersDTO);
        }
        return ResponseEntity.ok(featured);
    }

    /**
    @GetMapping("/orders/{firebaseID}")
    public ResponseEntity<List<SellerOrdersDTO>> getIndividualSellersbyFirebase(@PathVariable String firebaseID){
        int id = individualSellersService.getIndividualSellerIdByFirebaseID(firebaseID);
        return ResponseEntity.ok(individualSellersOrdersService.findByIndividualSellerID(id));
    }*/

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
