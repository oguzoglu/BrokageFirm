package com.example.brokagefrim.model;

import com.example.brokagefrim.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer extends AbstractBaseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zip;
    private String country;
    private LocalDateTime dateOfBirth;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}