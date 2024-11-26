package com.OrderTrackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OrderTrackingSystem.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
