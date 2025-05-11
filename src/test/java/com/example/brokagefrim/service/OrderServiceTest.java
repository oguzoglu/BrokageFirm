package com.example.brokagefrim.service;

import com.example.brokagefrim.exception.OrderCannotBeCanceledException;
import com.example.brokagefrim.exception.OrderNotFoundException;
import com.example.brokagefrim.model.Asset;
import com.example.brokagefrim.model.Order;
import com.example.brokagefrim.model.OrderSide;
import com.example.brokagefrim.model.OrderStatus;
import com.example.brokagefrim.repository.OrderRepository;
import com.example.brokagefrim.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Asset asset;

    @BeforeEach
    public void setUp() {
        asset = new Asset();
        asset.setId(1L);
        asset.setCustomerId(1L);
        asset.setAssetName("Asset1");
        asset.setSize(20L);
        asset.setUsableSize(15L);

        order = new Order();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setAssetName("Asset1");
        order.setSize(10L);
        order.setOrderSide(OrderSide.SELL);
        order.setStatus(OrderStatus.PENDING);
    }


    @Test
    public void testMatchOrder_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.matchOrder(1L);
        });
        assertEquals("Order with id 1 not found", exception.getMessage());
    }

    @Test
    public void testCreateOrder_Success() {
        when(assetService.findByCustomerIdAndAssetNameOp(1L, "Asset1")).thenReturn(Optional.of(asset));
        when(orderRepository.save(order)).thenReturn(order);
        Order createdOrder = orderService.createOrder(order);
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testCancelOrder_Success() {

        when(orderRepository.findByIdAndCustomerId(1L, 1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        Order canceledOrder = orderService.cancelOrder(1L, 1L);
        assertEquals(OrderStatus.CANCELED, canceledOrder.getStatus());
        verify(orderRepository, times(1)).save(canceledOrder);
    }

    @Test
    public void testCancelOrder_OrderNotFound() {
        when(orderRepository.findByIdAndCustomerId(1L, 1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.cancelOrder(1L, 1L);
        });
        assertEquals("Order with id 1 not found", exception.getMessage());
    }

    @Test
    public void testCancelOrder_CannotCancelNonPendingOrder() {
        order.setStatus(OrderStatus.MATCHED);
        when(orderRepository.findByIdAndCustomerId(1L, 1L)).thenReturn(Optional.of(order));

        OrderCannotBeCanceledException exception = assertThrows(OrderCannotBeCanceledException.class, () -> {
            orderService.cancelOrder(1L, 1L);
        });
        assertEquals("Order with id 1 cannot be canceled as it doesn't have pending status", exception.getMessage());
    }

    @Test
    public void testUpdateAssetSize_Success() {
        when(assetService.findByCustomerIdAndAssetNameOp(1L, "Asset1")).thenReturn(Optional.of(asset));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        var updatedSize = asset.getSize() - order.getSize();
        doNothing().when(assetService).updateSize(1L, updatedSize);
        when(orderRepository.save(order)).thenReturn(order);
        Order matchedOrder = orderService.matchOrder(1L);
        verify(assetService, times(1)).updateSize(1L, updatedSize);
        assertEquals(OrderStatus.MATCHED, matchedOrder.getStatus());
    }

    @Test
    public void testUpdateAssetUsableSize_Success() {
        when(assetService.findByCustomerIdAndAssetNameOp(1L, "Asset1")).thenReturn(Optional.of(asset));
        doNothing().when(assetService).updateUsableSize(1L, 5L);

        orderService.createOrder(order);
        verify(assetService, times(1)).updateUsableSize(1L, 5L);
    }

    @Test
    public void testAssetSubtractIsValid_AssetNotFound() {
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.matchOrder(1L);
        });
        assertEquals("Order with id 1 not found", exception.getMessage());
    }

}