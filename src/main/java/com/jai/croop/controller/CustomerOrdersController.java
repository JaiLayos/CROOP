package com.jai.croop.controller;

import com.jai.croop.model.*;
import com.jai.croop.service.ICustomerOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-orders")
public class CustomerOrdersController {
    @Autowired
    public ICustomerOrdersService customerOrdersService;

    @PostMapping("/group-seller")
    public ResponseEntity<CustomerOrdersForGroupSellers> addCustomerOrdersToGroup(@RequestBody CustomerOrdersForGroupSellers customerOrders,
                                                                                  Customer customer, GroupSellers groupSellers){
        System.out.println("Receive Customer Orders: " + customerOrders);
        return  ResponseEntity.ok(customerOrdersService.addCustomerOrdersToGroupOrders(customerOrders, customer, groupSellers));
    }

    @PostMapping("/individual-seller")
    public ResponseEntity<CustomerOrdersForIndivSellers> addCustomerOrdersToIndividualSellers(@RequestBody CustomerOrdersForIndivSellers customerOrders,
                                                                                              Customer customer, IndividualSellers individualSellers){
        System.out.println("Receive Customer Orders: " + customerOrders);
        return  ResponseEntity.ok(customerOrdersService.addCustomerOrdersToIndividualOrders(customerOrders, customer, individualSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrdersForGroupSellers> getCustomerOrders(@PathVariable int id){
        return ResponseEntity.ok(customerOrdersService.getCustomerOrders(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerOrdersForGroupSellers>> getAllCustomerOrders(){
        return ResponseEntity.ok(customerOrdersService.getAllCustomerOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerOrdersForGroupSellers> updateCustomerOrders(@PathVariable int id, @RequestBody CustomerOrdersForGroupSellers customerOrders){
        return ResponseEntity.ok((customerOrdersService.updateCustomerOrders(id, customerOrders)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerOrdersForGroupSellers> deleteCustomerOrders(@PathVariable int id){
        customerOrdersService.deleteCustomerOrders(id);
        return ResponseEntity.noContent().build();
    }

}
