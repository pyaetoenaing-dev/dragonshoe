package com.OrderTrackingSystem.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OrderTrackingSystem.model.Brand;
import com.OrderTrackingSystem.repository.BrandRepository;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }
    
    public void deleteBrandById(long brandId) {
    	brandRepository.deleteById(brandId);
    }
    
    public Brand getBrandById(long brandId) {
        Optional<Brand> optional = brandRepository.findById(brandId);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Product not found for id: " + brandId);
        }
    }
    
    public Brand findById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
    }
    
    public List<Brand> findByIds(List<Long> ids) {
        return brandRepository.findAllById(ids);
    }
    

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
    
    public List<Brand> getBrandsByIds(List<Long> brandId) {
        return brandRepository.findAllById(brandId);
    }

}
