package com.OrderTrackingSystem.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.OrderTrackingSystem.model.OrderItem;
import com.OrderTrackingSystem.repository.OrderItemRepository;

@Service
public class OrderItemService {
	@Autowired
    private OrderItemRepository orderItemRepository;
	
	public List<OrderItem> getOrderItemsByUserId(Long userId) {
	    return orderItemRepository.findByOrderUserId(userId);
	}
	
	public List<OrderItem> getOrderItems() {
        return orderItemRepository.findAll();
    }
	
	public List<OrderItem> findOrderItemsByOrderDate(LocalDate orderDate) {
        return orderItemRepository.findByOrder_OrderDate(orderDate);
    }

	public List<OrderItem> findByOrderDate(LocalDate date) {
        return orderItemRepository.findByOrderOrderDate(date);
    }
	
	public List<OrderItem> findByOrderMonth(YearMonth month) {
	    return orderItemRepository.findByOrderDateBetween(month.atDay(1), month.atEndOfMonth());
	}
	
	public List<OrderItem> findOrderItemsByOrderMonth(YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();
        return orderItemRepository.findByOrderDateBetween(startDate, endDate);
    }
	
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }
}
