package com.example.brokagefrim.dto.request;

import com.example.brokagefrim.model.OrderSide;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
    @NotNull(message = "asset name cannot be null")
    private String assetName;
    @NotNull(message = "order side cannot be null")
    private OrderSide orderSide;
    @NotNull(message = "size cannot be null")
    @Positive(message = "Size must be greater than zero")
    private Long size;
    @Positive(message = "Size must be greater than zero")
    @NotNull(message = "price cannot be null")
    private BigDecimal price;
}