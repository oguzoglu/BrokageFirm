package com.example.brokagefrim.service.impl;

import com.example.brokagefrim.model.Customer;
import com.example.brokagefrim.model.user.User;
import com.example.brokagefrim.repository.CustomerRepository;
import com.example.brokagefrim.repository.UserRepository;
import com.example.brokagefrim.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.map(value -> customerRepository.findCustomerByUserId(value.getId())).orElse(null);
    }
}
