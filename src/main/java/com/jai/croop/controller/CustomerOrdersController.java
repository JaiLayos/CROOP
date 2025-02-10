package com.jai.croop.controller;

import com.jai.croop.model.Customer;
import com.jai.croop.model.CustomerOrders;
import com.jai.croop.model.GroupSellers;
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

    @PostMapping
    public ResponseEntity<CustomerOrders> addCustomerOrders(@RequestBody CustomerOrders customerOrders,
                                                            Customer customer, GroupSellers groupSellers){
        System.out.println("Receive Customer Orders: " + customerOrders);
        return  ResponseEntity.ok(customerOrdersService.addCustomerOrdersToGroupOrders(customerOrders, customer, groupSellers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrders> getCustomerOrders(@PathVariable int id){
        return ResponseEntity.ok(customerOrdersService.getCustomerOrders(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerOrders>> getAllCustomerOrders(){
        return ResponseEntity.ok(customerOrdersService.getAllCustomerOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerOrders> updateCustomerOrders(@PathVariable int id, @RequestBody CustomerOrders customerOrders){
        return ResponseEntity.ok((customerOrdersService.updateCustomerOrders(id, customerOrders)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerOrders> deleteCustomerOrders(@PathVariable int id){
        customerOrdersService.deleteCustomerOrders(id);
        return ResponseEntity.noContent().build();
    }

}
