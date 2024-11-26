package com.OrderTrackingSystem.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.OrderTrackingSystem.model.CartItem;
import com.OrderTrackingSystem.model.ProductDetail;
import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.service.CartItemService;
import com.OrderTrackingSystem.service.LoginService;
import com.OrderTrackingSystem.service.ProductDetailService;
import com.OrderTrackingSystem.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private ProductDetailService productDetailService;
	
	@Autowired
    private CartItemService cartItemService;

    
    
	@PostMapping("/add")
	public String addToCart(@RequestParam Long userId, @RequestParam Long productDetailId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
	    
	    User user = userService.findById(userId);
	    ProductDetail productDetail = productDetailService.findById(productDetailId);

	    CartItem existingCartItem = cartItemService.findCartItemByUserAndProductDetail(user, productDetail);

	    int totalRequestedQuantity = (existingCartItem != null ? existingCartItem.getQuantity() : 0) + quantity;

	    if (totalRequestedQuantity > productDetail.getStockQty()) {
	        redirectAttributes.addFlashAttribute("error", "Sorry, we don't have enough stock to fulfill your requested quantity.");
	        redirectAttributes.addAttribute("productId", productDetail.getProduct().getProductId());
	        return "redirect:/user/viewProductDetail/{productId}";
	    }

	    if (existingCartItem == null) {
	        CartItem cartItem = new CartItem();
	        cartItem.setUser(user);
	        cartItem.setProductDetail(productDetail);
	        cartItem.setQuantity(quantity);
	        cartItemService.saveCartItem(cartItem);
	    } else {
	        existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
	        cartItemService.saveCartItem(existingCartItem);
	    }
	    redirectAttributes.addAttribute("productId", productDetail.getProduct().getProductId());
	    return "redirect:/user/viewProductDetail/{productId}";
	}
   
	@PostMapping("/updateCartItem")
    public String updateCartItemQuantity(@RequestParam Long cartItemId, @RequestParam int quantity) {
      
        CartItem cartItem = cartItemService.findCartItemById(cartItemId);
        
       
        if (cartItem == null) {
           
            return "redirect:/user/cart"; 
        }
       
        cartItem.setQuantity(quantity);
        cartItemService.saveCartItem(cartItem);
      
        return "redirect:/user/cart"; 
    } 
}
