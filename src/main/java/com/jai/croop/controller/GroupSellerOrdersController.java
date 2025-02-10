package com.jai.croop.controller;

import com.jai.croop.model.Customer;
import com.jai.croop.model.CustomerOrders;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import com.jai.croop.service.ICustomerOrdersService;
import com.jai.croop.service.IGroupSellersOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-seller-orders")
public class GroupSellerOrdersController {
    @Autowired
    public IGroupSellersOrdersService groupSellersOrdersService;

    @PostMapping
    public ResponseEntity<GroupSellersOrders> addGroupSellerOrders(@RequestBody GroupSellersOrders groupSellersOrders,
                                                            Customer customer, GroupSellers groupSellers){
        System.out.println("Receive Customer Orders: " + groupSellersOrders);
        return  ResponseEntity.ok(groupSellersOrdersService.addGroupSellerOrders(groupSellersOrders, customer, groupSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupSellersOrders> getGroupSellerOrders(@PathVariable int id){
        return ResponseEntity.ok(groupSellersOrdersService.getGroupSellerOrders(id));
    }

    @GetMapping
    public ResponseEntity<List<GroupSellersOrders>> getAllGroupSellerOrders(){
        return ResponseEntity.ok(groupSellersOrdersService.getAllGroupSellerOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupSellersOrders> updateCustomerOrders(@PathVariable int id, @RequestBody GroupSellersOrders groupSellersOrders){
        return ResponseEntity.ok((groupSellersOrdersService.updateGroupSellerOrders(id, groupSellersOrders)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupSellersOrders> deleteCustomerOrders(@PathVariable int id){
        groupSellersOrdersService.deleteGroupSellerOrders(id);
        return ResponseEntity.noContent().build();
    }
}
