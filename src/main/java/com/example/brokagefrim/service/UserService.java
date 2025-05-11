package com.example.brokagefrim.service;

import com.example.brokagefrim.model.user.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User save(User user);
}