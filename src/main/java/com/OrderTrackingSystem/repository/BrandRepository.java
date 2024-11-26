package com.OrderTrackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.OrderTrackingSystem.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
   
}

