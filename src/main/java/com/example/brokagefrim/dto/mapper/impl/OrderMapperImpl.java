package com.example.brokagefrim.dto.mapper.impl;

import com.example.brokagefrim.dto.OrderUpdateDto;
import com.example.brokagefrim.dto.mapper.OrderMapper;
import com.example.brokagefrim.dto.request.OrderRequestDto;
import com.example.brokagefrim.dto.response.OrderResponseDto;
import com.example.brokagefrim.model.Order;
import com.example.brokagefrim.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(OrderRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setAssetName(dto.getAssetName());
        order.setOrderSide(dto.getOrderSide());
        order.setSize(dto.getSize());
        order.setPrice(dto.getPrice());
        order.setStatus(OrderStatus.PENDING);

        return order;
    }

    @Override
    public OrderResponseDto toDto(Order entity) {
        if (entity == null) {
            return null;
        }

        OrderResponseDto responseDto = new OrderResponseDto();

        responseDto.setId(entity.getId());
        responseDto.setCustomerId(entity.getCustomerId() != null ? entity.getCustomerId().toString() : null);
        responseDto.setAssetName(entity.getAssetName());
        responseDto.setOrderSide(entity.getOrderSide());
        responseDto.setSize(entity.getSize());
        responseDto.setPrice(entity.getPrice());
        responseDto.setStatus(entity.getStatus());
        responseDto.setCreateDate(entity.getCreateDate());
        responseDto.setUpdateDate(entity.getUpdateDate());

        return responseDto;
    }

    @Override
    public void updateStatusFromDto(OrderUpdateDto dto, Order entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
    }
}