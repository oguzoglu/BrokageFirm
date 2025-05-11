package com.example.brokagefrim.controller;

import com.example.brokagefrim.dto.request.LoginRequest;
import com.example.brokagefrim.dto.response.LoginResponse;
import com.example.brokagefrim.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginDto) {
        String token = authService.login(loginDto);
        LoginResponse authResponseDto = new LoginResponse();
        authResponseDto.setToken(token);
        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }
}
