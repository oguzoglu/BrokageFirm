package com.example.brokagefrim.service.impl;

import com.example.brokagefrim.exception.AssetNotFoundException;
import com.example.brokagefrim.exception.InsufficientAssetException;
import com.example.brokagefrim.exception.OrderCannotBeCanceledException;
import com.example.brokagefrim.exception.OrderNotFoundException;
import com.example.brokagefrim.model.*;
import com.example.brokagefrim.repository.OrderRepository;
import com.example.brokagefrim.service.AssetService;
import com.example.brokagefrim.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AssetService assetService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, AssetService assetService) {
        this.orderRepository = orderRepository;
        this.assetService = assetService;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public Order findByIdAndCustomerId(Long orderId, Long customerId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId).orElse(null);
    }


    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> findByCustomerIdAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
    }

    @Override
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    @Transactional
    public Order matchOrder(Long id) {

        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == OrderStatus.PENDING) {
                updateAssetSize(order);
                order.setStatus(OrderStatus.MATCHED);
                return orderRepository.save(order);
            } else {
                throw new OrderCannotBeCanceledException(String.format("Order with id %s cannot be canceled as it doesn't have pending status", id));
            }
        } else {
            throw new OrderNotFoundException(String.format("Order with id %s not found", id));
        }
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        if (order.getOrderSide() == OrderSide.SELL) {
            subtractAssetUsableSize(order);
        }
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long customerId, Long orderId) {
        Optional<Order> orderOpt = orderRepository.findByIdAndCustomerId(orderId, customerId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == OrderStatus.PENDING) {
                order.setStatus(OrderStatus.CANCELED);
                updateAssetUsableSize(order);
                return orderRepository.save(order);
            } else {
                throw new OrderCannotBeCanceledException(String.format("Order with id %s cannot be canceled as it doesn't have pending status", orderId));
            }
        } else {
            throw new OrderNotFoundException(String.format("Order with id %s not found", orderId));
        }
    }

    private void updateAssetSize(Order order) {
        Optional<Asset> result = assetService.findByCustomerIdAndAssetNameOp(order.getCustomerId(), order.getAssetName());
        if (order.getOrderSide().equals(OrderSide.SELL)) {
            Asset asset = assetSubtractIsValid(order, result);
            assetService.updateSize(asset.getId(), asset.getSize() - order.getSize());
        } else if (order.getOrderSide().equals(OrderSide.BUY)) {
            Optional<Asset> assetOptional = assetService.findByCustomerIdAndAssetNameOp(order.getCustomerId(), order.getAssetName());
            if (assetOptional.isPresent()) {
                var newSize = assetOptional.get().getSize() + (order.getSize());
                assetService.updateSize(assetOptional.get().getId(), newSize);
                assetService.updateUsableSize(assetOptional.get().getId(), newSize);
            } else {
                Asset asset = new Asset();
                asset.setCustomerId(order.getCustomerId());
                asset.setAssetName(order.getAssetName());
                asset.setSize(order.getSize());
                asset.setUsableSize(order.getSize());
                asset.setCurrency(Currency.TRY);
                assetService.save(asset);
            }

        }
    }

    private void subtractAssetUsableSize(Order order) {
        Optional<Asset> result = assetService.findByCustomerIdAndAssetNameOp(order.getCustomerId(), order.getAssetName());
        Asset asset = assetSubtractIsValid(order, result);
        assetService.updateUsableSize(asset.getId(), asset.getUsableSize() - (order.getSize()));
    }

    private void updateAssetUsableSize(Order order) {
        if (order.getOrderSide() == OrderSide.SELL){
            Optional<Asset> asset = assetService.findByCustomerIdAndAssetNameOp(order.getCustomerId(), order.getAssetName());
            asset.ifPresent(value -> assetService.updateUsableSize(value.getId(), value.getUsableSize() + order.getSize()));
        }
    }

    private Asset assetSubtractIsValid(Order order, Optional<Asset> asset) {
        if (asset.isEmpty()) {
            throw new AssetNotFoundException(String.format("asset of name %s not found ", order.getAssetName()));
        } else if (asset.get().getUsableSize().compareTo(order.getSize()) < 0) {
            throw new InsufficientAssetException(String.format("Insufficient usable %s to sell %s units.", order.getOrderSide(), order.getSize()));
        }
        return asset.get();
    }
}