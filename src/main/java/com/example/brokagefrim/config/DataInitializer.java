package com.example.brokagefrim.config;


import com.example.brokagefrim.dto.request.RegisterRequest;
import com.example.brokagefrim.model.Asset;
import com.example.brokagefrim.model.Currency;
import com.example.brokagefrim.model.Customer;
import com.example.brokagefrim.model.user.Role;
import com.example.brokagefrim.model.user.User;
import com.example.brokagefrim.repository.AssetRepository;
import com.example.brokagefrim.repository.CustomerRepository;
import com.example.brokagefrim.repository.RoleRepository;
import com.example.brokagefrim.repository.UserRepository;
import com.example.brokagefrim.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            AuthService authService,
            UserRepository userRepository,
            RoleRepository roleRepository,
            CustomerRepository customerRepository,
            AssetRepository assetRepository) {

        return args -> {

            if (userRepository.count() > 0) {
                return; // Database has already been seeded
            }

            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);

            // Create users
            RegisterRequest adminUser = new RegisterRequest();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin123");
            adminUser.setEmail("admin@brokagefirm.com");
            User registeredAdmin = authService.register(adminUser, "ADMIN");


            RegisterRequest Omer = new RegisterRequest();
            Omer.setUsername("Omer");
            Omer.setPassword("user123");
            Omer.setEmail("Omer@brokagefirm.com");
            User registeredOmer = authService.register(Omer, "USER");

            RegisterRequest Ahmet = new RegisterRequest();
            Ahmet.setUsername("Ahmet");
            Ahmet.setPassword("user123");
            Ahmet.setEmail("Ahmet@brokagefirm.com");
            User registeredAhmet = authService.register(Ahmet, "USER");

            RegisterRequest Faruk = new RegisterRequest();
            Faruk.setUsername("Faruk");
            Faruk.setPassword("user123");
            Faruk.setEmail("Faruk@brokagefirm.com");
            User registeredFaruk = authService.register(Faruk, "USER");

            // Create some customers
            Customer customer1 = new Customer();
            customer1.setFirstName("Omer");
            customer1.setLastName("mm");
            customer1.setEmail("omer.m@example.com");
            customer1.setPhone("123-456-7890");
            customer1.setAddress("xxx sokak no 505");
            customer1.setCity("Sisli");
            customer1.setZip("xxxxx");
            customer1.setCountry("TR");
            customer1.setDateOfBirth(LocalDateTime.now().minusYears(30));
            customer1.setUser(registeredOmer);
            customerRepository.save(customer1);

            Customer customer2 = new Customer();
            customer2.setFirstName("Ahmet");
            customer2.setLastName("xxx");
            customer2.setEmail("xxx.ahmet@example.com");
            customer2.setPhone("xxx-xxx-xxx");
            customer2.setAddress("address xx yy");
            customer2.setCity("Beylikduzu");
            customer2.setZip("100001");
            customer2.setCountry("TR");
            customer2.setDateOfBirth(LocalDateTime.now().minusYears(25));
            customer2.setUser(registeredAhmet);
            customerRepository.save(customer2);

            Customer customer3 = new Customer();
            customer3.setFirstName("Faruk");
            customer3.setLastName("xxx");
            customer3.setEmail("xxx.hakan@example.com");
            customer3.setPhone("xxx-xxx-xxx");
            customer3.setAddress("address xx yy");
            customer3.setCity("Beyoglu");
            customer3.setZip("100001");
            customer3.setCountry("TR");
            customer3.setDateOfBirth(LocalDateTime.now().minusYears(25));
            customer3.setUser(registeredFaruk);
            customerRepository.save(customer3);

            // Create some assets
            Asset asset1 = new Asset();
            asset1.setAssetName("A");
            asset1.setCurrency(Currency.TRY);
            asset1.setSize(1000L);
            asset1.setUsableSize(1000L);
            asset1.setCustomerId(customer1.getId());
            assetRepository.save(asset1);

            Asset asset2 = new Asset();
            asset2.setAssetName("B");
            asset2.setCurrency(Currency.TRY);
            asset2.setSize(500L);
            asset2.setUsableSize(500L);
            asset2.setCustomerId(customer2.getId());
            assetRepository.save(asset2);

            Asset asset3 = new Asset();
            asset3.setAssetName("B");
            asset3.setCurrency(Currency.USD);
            asset3.setSize(600L);
            asset3.setUsableSize(600L);
            asset3.setCustomerId(customer2.getId());
            assetRepository.save(asset3);

            System.out.println("Database has been seeded successfully!");
        };
    }
}