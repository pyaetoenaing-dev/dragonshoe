package com.OrderTrackingSystem.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.OrderTrackingSystem.model.CartItem;
import com.OrderTrackingSystem.model.ProductDetail;
import com.OrderTrackingSystem.model.User;

import jakarta.transaction.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
	@Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user.userId = :userId")
    Integer sumQuantityByUserId(@Param("userId") Long userId);
	List<CartItem> findByUserUserId(Long userId);
	@Query("SELECT c FROM CartItem c WHERE c.user.id = :userId AND c.productDetail.productDetailId = :productDetailId")
    CartItem findByUserIdAndProductDetailId(@Param("userId") Long userId, @Param("productDetailId") Long productDetailId);
	@Transactional
    void deleteByUser(User user);
	List<CartItem> findByUser(User user);
	CartItem findByUserAndProductDetail(User user, ProductDetail productDetail);
	
}

