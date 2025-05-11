package com.example.brokagefrim.controller;

import com.example.brokagefrim.dto.mapper.OrderMapper;
import com.example.brokagefrim.dto.request.OrderRequestDto;
import com.example.brokagefrim.dto.response.OrderResponseDto;
import com.example.brokagefrim.exception.InsufficientAssetException;
import com.example.brokagefrim.exception.OrderNotFoundException;
import com.example.brokagefrim.model.Order;
import com.example.brokagefrim.model.OrderStatus;
import com.example.brokagefrim.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        try {
            Order order = orderMapper.toEntity(orderRequestDto);
            order.setStatus(OrderStatus.PENDING);
            Order savedOrder = orderService.createOrder(order);
            return new ResponseEntity<>(orderMapper.toDto(savedOrder), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Order> orders;

        if (customerId != null && startDate != null && endDate != null) {
            orders = orderService.findByCustomerIdAndDateRange(customerId, startDate, endDate);
        } else if (customerId != null) {
            orders = orderService.findByCustomerId(customerId);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<OrderResponseDto> orderDtoList = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @GetMapping("/{customerId}/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderByCustomerIdAndId(
            @PathVariable(required = true)Long customerId,
            @PathVariable(required = true) Long orderId) {
        Order order = orderService.findByIdAndCustomerId(orderId, customerId);
        return new ResponseEntity<>(orderMapper.toDto(order), HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<OrderResponseDto>> listAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) OrderStatus status) {

        List<Order> orders;

        if (startDate != null && endDate != null) {
            orders = orderService.findByDateRange(startDate, endDate);
        } else if (status != null) {
            orders = orderService.findByStatus(status);
        } else {
            orders = orderService.findAll();
        }

        List<OrderResponseDto> orderDtoList = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @GetMapping("/cancel/{customerId}/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long customerId, @PathVariable Long id) {
        try {
            OrderResponseDto orderResponseDto = orderMapper.toDto(orderService.cancelOrder(customerId, id));
            return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/match/{id}")
    public ResponseEntity<OrderResponseDto> matchOrder(@PathVariable Long id) {
        OrderResponseDto orderResponseDto = orderMapper.toDto(orderService.matchOrder(id));
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }
}