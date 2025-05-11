package com.example.brokagefrim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDto {

    // User
    private String name;
    private String username;
    private String email;
    private String password;

    // Customer
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private LocalDateTime dateOfBirth;
}
