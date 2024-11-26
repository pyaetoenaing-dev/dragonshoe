package com.OrderTrackingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OrderTrackingSystem.model.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    
    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.productName LIKE %:keyword%")
    List<ProductDetail> findByProductNameKeyword(String keyword);

}

