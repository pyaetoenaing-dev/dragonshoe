package com.OrderTrackingSystem.repository;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.OrderTrackingSystem.model.Order;
import com.OrderTrackingSystem.model.OrderItem;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByOrderDate(LocalDate orderDate);
	@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate = :date")
    Optional<BigDecimal> sumTotalAmountByOrderDate(@Param("date") LocalDate date);
	@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
	Optional<BigDecimal> sumTotalAmountByOrderMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

