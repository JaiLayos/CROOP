package com.jai.croop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jai.croop.model.Customer;
import com.jai.croop.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService implements ICustomerService{
    @Autowired
    private CustomerRepository customerRep;
    @Override
    public List<Customer> getAllCustomers(){
        return customerRep.findAll();
    }

    @Override
    public Customer addCustomer(Customer customer){
        return customerRep.save(customer);
    }

    @Override
    public Customer getCustomer(int id){
       return customerRep.findById(id).orElseThrow(() -> new RuntimeException("Customer Not Found."));
    }

    @Transactional
    @Override
    public Customer updateCustomer(int id, Customer updatedCustomer){
        Date date = new Date();
        Customer customer = getCustomer(id);
        customer.setName(updatedCustomer.getName());
        customer.setAddress(updatedCustomer.getAddress());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setBio(updatedCustomer.getBio());
        customer.setRoles(updatedCustomer.getRoles());
        customer.setUpdatedAt(date);
        return customerRep.save(customer);    
    }

    @Transactional
    @Override
    public void deleteCustomer(int id){
        customerRep.deleteById(id);
    }
}