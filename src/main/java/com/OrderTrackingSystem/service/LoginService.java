package com.OrderTrackingSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OrderTrackingSystem.model.Role;
import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.repository.RoleRepository;
import com.OrderTrackingSystem.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User registerUser(User user) {
        Role userRole = roleRepository.findByRoleName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
        }
        user.setRole(userRole);
        user.setAccStatus("User");
        return userRepository.save(user);
    }
    
    public User registerAdmin(User user) {
        Role adminRole = roleRepository.findByRoleName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);
        }
        user.setRole(adminRole);
        user.setAccStatus("Administrator");
        return userRepository.save(user);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean isUserRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }
    
    public User getUserById(Long id) {
        
        return userRepository.findById(id).orElse(null);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	public List<User> getUsers() {
		
		return userRepository.findAll();
	}
	
	public User getLoggedInUser(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        return (User) session.getAttribute("loggedInUser");
	    }
	    return null;
	}
	
	public boolean isEmailExists(String email) {
        User existingUser = userRepository.findByEmail(email);
        return existingUser != null;
    }

}
