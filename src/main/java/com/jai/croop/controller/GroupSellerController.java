package com.jai.croop.controller;

import com.jai.croop.model.FeaturedSellersDTO;
import com.jai.croop.model.SellerOrdersDTO;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.service.GroupSellersOrdersService;
import com.jai.croop.service.IGroupSellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/group-sellers")
public class GroupSellerController {

    @Autowired
    public IGroupSellersService groupSellersService;

    @Autowired
    public GroupSellersOrdersService groupSellersOrdersService;

    @PostMapping
    public ResponseEntity<GroupSellers> addGroupSellers(@RequestBody GroupSellers groupSellers){
        System.out.println("Receive Group Seller: " + groupSellers);
        return ResponseEntity.ok(groupSellersService.addGroupSellers(groupSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupSellers> getGroupSellers(@PathVariable int id){
        return ResponseEntity.ok(groupSellersService.getGroupSellers(id));
    }

    @GetMapping("/id/{firebaseID}")
    public ResponseEntity<Integer> getGroupSellersID(@PathVariable String firebaseID){
        return ResponseEntity.ok(groupSellersService.findIDByFirebaseID(firebaseID));
    }

    @GetMapping("/orders/{firebaseID}")
    public ResponseEntity<List<SellerOrdersDTO>> getGroupSellersbyFirebase(@PathVariable String firebaseID){
        int id = groupSellersService.getGroupSellerIdByFirebaseID(firebaseID);
        return ResponseEntity.ok(groupSellersOrdersService.findByGroupSellerID(id));
    }

    @GetMapping("/details/{firebaseID}")
    public ResponseEntity<GroupSellers> getGroupSellersbyFirebaseID(@PathVariable String firebaseID){
        int id = groupSellersService.getGroupSellerIdByFirebaseID(firebaseID);
        return ResponseEntity.ok(groupSellersService.getGroupSellers(id));
    }


    @GetMapping("/group-name/{groupName}")
    public ResponseEntity<List<FeaturedSellersDTO>> getGroupSellerByGroupName(@PathVariable String groupName){
        List<FeaturedSellersDTO> featuredSellersDTO = new ArrayList<>();
        List<GroupSellers> groupSellers = groupSellersService.findByGroupName(groupName);
        for(GroupSellers groupSeller : groupSellers){
            FeaturedSellersDTO featured = new FeaturedSellersDTO();
            featured.setId(groupSeller.getId());
            featured.setName(groupSeller.getGroupName());
            featured.setRole(groupSeller.getRoles());
            featured.setFirebaseID(groupSeller.getFirebaseID());
            featuredSellersDTO.add(featured);
        }
        return ResponseEntity.ok(featuredSellersDTO);
    }

    @GetMapping
    public ResponseEntity<List<GroupSellers>> getAllGroupSellers(){
        return ResponseEntity.ok(groupSellersService.getAllGroupSellers());
    }

    @GetMapping("/dto")
    public ResponseEntity<List<FeaturedSellersDTO>> getAllSellers(){
        List<FeaturedSellersDTO> featured = new ArrayList<>();
        List<GroupSellers> groupSellers = groupSellersService.getAllGroupSellers();
        for(GroupSellers groupSeller : groupSellers){
            FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
            featuredSellersDTO.setId(groupSeller.getId());
            featuredSellersDTO.setName(groupSeller.getGroupName());
            featuredSellersDTO.setRole(groupSeller.getRoles());
            featuredSellersDTO.setFirebaseID(groupSeller.getFirebaseID());
            featured.add(featuredSellersDTO);
        }
        return ResponseEntity.ok(featured);
    }
    @GetMapping("/featured")
    public ResponseEntity<List<FeaturedSellersDTO>> getFeaturedGroup(){
        List<GroupSellers> groupSellers = groupSellersService.findTop3ByTotalItemStart();
        List<FeaturedSellersDTO> featured = new ArrayList<>();
        for(GroupSellers groupSeller: groupSellers){
            FeaturedSellersDTO featuredSellersDTO = new FeaturedSellersDTO();
            featuredSellersDTO.setId(groupSeller.getId());
            featuredSellersDTO.setName(groupSeller.getGroupName());
            featuredSellersDTO.setRole(groupSeller.getRoles());
            featuredSellersDTO.setFirebaseID(groupSeller.getFirebaseID());
            featured.add(featuredSellersDTO);
        }
        return ResponseEntity.ok(featured);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupSellers> updateGroupSellers(@PathVariable int id, @RequestBody GroupSellers groupSellers){
        return ResponseEntity.ok((groupSellersService.updateGroupSellers(id, groupSellers)));
    }

    @PutMapping("/firebase/{firebaseID}")
    public ResponseEntity<GroupSellers> updateGroupSellersByFirebaseID(@PathVariable String firebaseID, @RequestBody GroupSellers groupSellers){
        return ResponseEntity.ok((groupSellersService.updateGroupSellersByFirebaseID(firebaseID, groupSellers)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupSellers> deleteGroupSellers(@PathVariable int id){
        groupSellersService.deleteGroupSellers(id);
        return ResponseEntity.noContent().build();
    }
}
