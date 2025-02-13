package com.jai.croop.controller;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import com.jai.croop.service.IGroupSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-sellers")
public class GroupSellerController {

    @Autowired
    public IGroupSellersService groupSellersService;

    @PostMapping
    public ResponseEntity<GroupSellers> addGroupSellers(@RequestBody GroupSellers groupSellers){
        System.out.println("Receive Group Seller: " + groupSellers);
        return ResponseEntity.ok(groupSellersService.addGroupSellers(groupSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupSellers> getGroupSellers(@PathVariable int id){
        return ResponseEntity.ok(groupSellersService.getGroupSellers(id));
    }

    @GetMapping("/orders/{firebaseID}")
    public ResponseEntity<GroupSellersOrders> getGroupSellersbyFirebase(@PathVariable String firebaseID){
        return ResponseEntity.ok(groupSellersService.getGroupSellerIdByFirebaseID(firebaseID));
    }

    @GetMapping
    public ResponseEntity<List<GroupSellers>> getAllGroupSellers(){
        return ResponseEntity.ok(groupSellersService.getAllGroupSellers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupSellers> updateGroupSellers(@PathVariable int id, @RequestBody GroupSellers groupSellers){
        return ResponseEntity.ok((groupSellersService.updateGroupSellers(id, groupSellers)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupSellers> deleteGroupSellers(@PathVariable int id){
        groupSellersService.deleteGroupSellers(id);
        return ResponseEntity.noContent().build();
    }
}
