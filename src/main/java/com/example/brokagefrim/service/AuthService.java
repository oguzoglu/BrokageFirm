package com.example.brokagefrim.service;

import com.example.brokagefrim.dto.request.LoginRequest;
import com.example.brokagefrim.dto.request.RegisterRequest;
import com.example.brokagefrim.model.user.User;

public interface AuthService {
    String login(LoginRequest loginDto);
    User register(RegisterRequest registerDto, String role);
}
