package com.jai.croop.controller;

import com.jai.croop.model.Customer;
import com.jai.croop.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    // Get all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // Get a single customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }
    @GetMapping("/name/{id}")
    public ResponseEntity<String> getCustomerName(@PathVariable int id) {
        Customer customer = customerService.getCustomer(id);
        return ResponseEntity.ok(customer.getName());
    }

    // Add a new customer
    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        System.out.println("Received Customer: " + customer);
        return ResponseEntity.ok(customerService.addCustomer(customer));
    }

    // Update an existing customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    // Delete a customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
