package com.example.brokagefrim.dto.response;

import com.example.brokagefrim.model.OrderSide;
import com.example.brokagefrim.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String customerId;
    private String assetName;
    private OrderSide orderSide;
    private Long size;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}