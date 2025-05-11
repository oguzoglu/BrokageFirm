package com.example.brokagefrim.dto.mapper;

import com.example.brokagefrim.dto.OrderUpdateDto;
import com.example.brokagefrim.dto.request.OrderRequestDto;
import com.example.brokagefrim.dto.response.OrderResponseDto;
import com.example.brokagefrim.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequestDto dto);

    OrderResponseDto toDto(Order entity);

    void updateStatusFromDto(OrderUpdateDto dto, @MappingTarget Order entity);
}