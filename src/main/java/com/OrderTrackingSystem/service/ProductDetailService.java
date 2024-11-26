package com.OrderTrackingSystem.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OrderTrackingSystem.model.ProductDetail;
import com.OrderTrackingSystem.repository.ProductDetailRepository;

@Service
public class ProductDetailService {
	
	@Autowired
    private ProductDetailRepository productDetailRepository;
	
	public ProductDetail saveProductDetail(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }
	
	public void deleteProductDetailById(long productDetailId) {
		productDetailRepository.deleteById(productDetailId);
    }
	
	public List<ProductDetail> getAllProductDetail() {
        return productDetailRepository.findAll();
    }
	
	public List<ProductDetail> getAllProductDetails(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return productDetailRepository.findByProductNameKeyword(keyword);
        }
        return productDetailRepository.findAll();
    }
	
	public ProductDetail findById(Long id) {
        return productDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductDetail not found"));
    }

	
}

