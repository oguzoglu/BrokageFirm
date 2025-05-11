package com.example.brokagefrim.service.impl;

import com.example.brokagefrim.dto.request.LoginRequest;
import com.example.brokagefrim.dto.request.RegisterRequest;
import com.example.brokagefrim.model.user.Role;
import com.example.brokagefrim.model.user.User;
import com.example.brokagefrim.repository.RoleRepository;
import com.example.brokagefrim.repository.UserRepository;
import com.example.brokagefrim.security.JwtTokenProvider;
import com.example.brokagefrim.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public User register(RegisterRequest registerDto, String role) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = User.builder().name(registerDto.getName()).username(registerDto.getUsername()).email(registerDto.getEmail()).password(passwordEncoder.encode(registerDto.getPassword())).build();

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(role).orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(userRole);
        user.setRoles(roles);
        return userRepository.save(user);
    }


}