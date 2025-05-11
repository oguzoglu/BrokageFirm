package com.example.brokagefrim.dto.mapper;


import com.example.brokagefrim.dto.request.CustomerRequestDto;
import com.example.brokagefrim.dto.response.CustomerResponseDto;
import com.example.brokagefrim.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer entity);

    void updateEntityFromDto(CustomerRequestDto dto, @MappingTarget Customer entity);
}