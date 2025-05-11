package com.example.brokagefrim.config;

import com.example.brokagefrim.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("authorization")
public class AuthorizationEvaluator {

    @Autowired
    private CustomerService customerService;

    public boolean isCustomerOwner(Long customerId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // if admin
        if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))) {
            return true;
        }

        // standard user
        if (auth.getPrincipal() instanceof UserDetails user) {
            var customer = customerService.findByUsername(user.getUsername());
            return customer.getId().equals(customerId);
        }
        return false;
    }
}
