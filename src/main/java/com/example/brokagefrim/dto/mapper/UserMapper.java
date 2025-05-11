package com.example.brokagefrim.dto.mapper;

import com.example.brokagefrim.dto.request.UserRequestDto;
import com.example.brokagefrim.dto.response.UserResponseDto;
import com.example.brokagefrim.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);

    UserResponseDto toDto(User entity);

    void updateEntityFromDto(UserRequestDto dto, @MappingTarget User entity);
}