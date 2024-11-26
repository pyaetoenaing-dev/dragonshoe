package com.OrderTrackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OrderTrackingSystem.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

