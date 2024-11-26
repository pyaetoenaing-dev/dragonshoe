package com.OrderTrackingSystem.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;
	
	public User getUserById(long userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("User not found for id: " + userId);
        }
    }
	
	public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
	
	
}
