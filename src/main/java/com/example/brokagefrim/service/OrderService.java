package com.example.brokagefrim.service;

import com.example.brokagefrim.model.Order;
import com.example.brokagefrim.model.OrderStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Order> findAll();

    Optional<Order> findById(Long id);

    Order save(Order order);

    @PreAuthorize("hasAuthority('ADMIN') or @authorization.isCustomerOwner(#customerId)")
    List<Order> findByCustomerId(Long customerId);

    @PreAuthorize("hasAuthority('ADMIN') or @authorization.isCustomerOwner(#customerId)")
    Order findByIdAndCustomerId(Long orderId, Long customerId);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Order> findByStatus(OrderStatus status);

    @PreAuthorize("hasAuthority('ADMIN') or @authorization.isCustomerOwner(#customerId)")
    List<Order> findByCustomerIdAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @PreAuthorize("hasAuthority('ADMIN')")
    Order matchOrder(Long id);

    @PreAuthorize("hasAuthority('ADMIN') or @authorization.isCustomerOwner(#order.customerId)")
    Order createOrder(Order order);

    @PreAuthorize("hasAuthority('ADMIN') or @authorization.isCustomerOwner(#customerId)")
    Order cancelOrder(Long customerId, Long id);
}