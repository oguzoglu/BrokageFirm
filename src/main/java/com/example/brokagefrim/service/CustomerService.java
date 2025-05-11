package com.example.brokagefrim.service;

import com.example.brokagefrim.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer save(Customer customer);
    Customer findByUsername(String userName);
}
