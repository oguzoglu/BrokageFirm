package com.example.brokagefrim.dto.mapper.impl;

import com.example.brokagefrim.dto.mapper.UserMapper;
import com.example.brokagefrim.dto.request.UserRequestDto;

import com.example.brokagefrim.dto.response.UserResponseDto;
import com.example.brokagefrim.model.user.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRequestDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setUsername(dto.getUsername());

        if (dto.getRoles() != null) {
            user.setRoles(new HashSet<>(dto.getRoles()));
        } else {
            user.setRoles(new HashSet<>());
        }

        return user;
    }

    @Override
    public UserResponseDto toDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(entity.getId());
        responseDto.setUsername(entity.getUsername());
        if (entity.getRoles() != null) {
            responseDto.setRoles(new HashSet<>(entity.getRoles()));
        }

        return responseDto;
    }

    @Override
    public void updateEntityFromDto(UserRequestDto dto, User entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getUsername() != null) {
            entity.setUsername(dto.getUsername());
        }
        if (dto.getRoles() != null) {
            entity.getRoles().clear();
            entity.getRoles().addAll(dto.getRoles());
        }
    }
}