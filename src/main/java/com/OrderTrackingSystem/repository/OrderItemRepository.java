package com.OrderTrackingSystem.repository;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.OrderTrackingSystem.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.user.userId = :userId")
    List<OrderItem> findByOrderUserId(@Param("userId") Long userId);
	
	List<OrderItem> findByOrder_OrderDate(LocalDate orderDate);
	
	List<OrderItem> findByOrderOrderDate(LocalDate date);
	
	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderDate BETWEEN :startDate AND :endDate")
	List<OrderItem> findByOrderDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
