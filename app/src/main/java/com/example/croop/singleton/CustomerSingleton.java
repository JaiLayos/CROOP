package com.example.croop.singleton;

import com.example.croop.model.Customer;

public class CustomerSingleton {
    private static CustomerSingleton instance;
    private Customer customer;

    private CustomerSingleton() {}

    public static synchronized CustomerSingleton getInstance() {
        if (instance == null) {
            instance = new CustomerSingleton();
        }
        return instance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

