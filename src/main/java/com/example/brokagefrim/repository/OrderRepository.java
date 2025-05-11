package com.example.brokagefrim.repository;

import com.example.brokagefrim.model.Order;
import com.example.brokagefrim.model.OrderSide;
import com.example.brokagefrim.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus status);
    Optional<Order> findByIdAndCustomerId(Long orderId, Long customerId);

    @Query("SELECT o FROM Order o WHERE o.createDate BETWEEN :startDate AND :endDate")
    List<Order> findByDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId AND o.createDate BETWEEN :startDate AND :endDate")
    List<Order> findByCustomerIdAndDateBetween(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
