package com.example.brokagefrim.dto.response;

import com.example.brokagefrim.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private Set<Role> roles;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}