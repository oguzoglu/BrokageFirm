package com.example.brokagefrim.dto;

import com.example.brokagefrim.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDto {
    private OrderStatus status;
}