package com.example.brokagefrim.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Order extends AbstractBaseModel{

    private Long customerId;
    private String assetName;

    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    private Long size;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}