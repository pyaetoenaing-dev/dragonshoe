package com.OrderTrackingSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.OrderTrackingSystem.model.Product;
import com.OrderTrackingSystem.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
        
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
    }
    
    public List<Product> getAllProducts(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByKeyword(keyword);
        }
        return productRepository.findAll();
    }
    
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    
    public String getProductImagePath(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            return productOpt.get().getPhotosImagePath(); 
        }
        return null; 
    }
       
    public void deleteProductById(long productId) {
    	productRepository.deleteById(productId);
    }
    
    public Product getProductById(long productId) {
        Optional<Product> optional = productRepository.findById(productId);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Product not found for id: " + productId);
        }
    }
    
    public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return productRepository.findAll(pageable);
    } 
}
