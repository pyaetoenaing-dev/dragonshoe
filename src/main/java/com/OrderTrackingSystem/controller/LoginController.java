package com.OrderTrackingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {	
	
	@Autowired
    private LoginService loginService;	
	@GetMapping("/login")
    public String showLoginForm() {
        return "auth-login-basic";
    }   
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpServletRequest request,
                            Model model) {
        HttpSession session = request.getSession(false);
        User user = loginService.authenticate(email, password);
        
        if (user == null) {
            model.addAttribute("error", "Invalid email or password.");
            return "auth-login-basic";
        }
        
        if (user.getRole().getRoleName().equals("USER")) {
        	session = request.getSession(true);
        	session.setAttribute("userId", user.getUserId());
            return "redirect:/user/home";
        } else {
        	switch (user.getType()) {
	            case "Production Manager":
	                return "redirect:/admin/productionManagerOrder";
	            case "Clerk":
	                return "redirect:/admin/clerk";
	            case "Chief Accountant":
	                return "redirect:/admin/chiefAccountant";
	            default:
	                model.addAttribute("error", "Unsupported user type.");
	                return "login";
	        }
        }   
    }
}
