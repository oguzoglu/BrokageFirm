package com.example.brokagefrim.dto.mapper.impl;

import com.example.brokagefrim.dto.mapper.CustomerMapper;
import com.example.brokagefrim.dto.request.CustomerRequestDto;
import com.example.brokagefrim.dto.response.CustomerResponseDto;
import com.example.brokagefrim.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Customer customer = new Customer();

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setZip(dto.getZip());
        customer.setCountry(dto.getCountry());
        customer.setDateOfBirth(dto.getDateOfBirth());

        return customer;
    }

    @Override
    public CustomerResponseDto toDto(Customer entity) {
        if (entity == null) {
            return null;
        }

        CustomerResponseDto responseDto = new CustomerResponseDto();

        responseDto.setId(entity.getId());
        responseDto.setFirstName(entity.getFirstName());
        responseDto.setLastName(entity.getLastName());
        responseDto.setEmail(entity.getEmail());
        responseDto.setPhone(entity.getPhone());
        responseDto.setAddress(entity.getAddress());
        responseDto.setCity(entity.getCity());
        responseDto.setZip(entity.getZip());
        responseDto.setCountry(entity.getCountry());
        responseDto.setDateOfBirth(entity.getDateOfBirth());
        responseDto.setCreateDate(entity.getCreateDate());
        responseDto.setUpdateDate(entity.getUpdateDate());

        return responseDto;
    }

    @Override
    public void updateEntityFromDto(CustomerRequestDto dto, Customer entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }
        if (dto.getZip() != null) {
            entity.setZip(dto.getZip());
        }
        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }
        if (dto.getDateOfBirth() != null) {
            entity.setDateOfBirth(dto.getDateOfBirth());
        }
    }
}