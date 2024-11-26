package com.OrderTrackingSystem.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.OrderTrackingSystem.FileUploadUtil;
import com.OrderTrackingSystem.model.Brand;
import com.OrderTrackingSystem.model.CartItem;
import com.OrderTrackingSystem.model.Order;
import com.OrderTrackingSystem.model.OrderItem;
import com.OrderTrackingSystem.model.Product;
import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.service.CartItemService;
import com.OrderTrackingSystem.service.LoginService;
import com.OrderTrackingSystem.service.OrderItemService;
import com.OrderTrackingSystem.service.OrderService;
import com.OrderTrackingSystem.service.ProductService;
import com.OrderTrackingSystem.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@GetMapping("/registerUser")
	public String showUserRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "user_registration_form";
	}

	@PostMapping("/registerUser")
	public String registerUser(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
		if (loginService.isUserRegistered(user.getEmail())) {
			model.addAttribute("error", "User with this email already registered.");
			return "user_registration_form";
		}
		loginService.registerUser(user);
		redirectAttributes.addFlashAttribute("registrationSuccess", true);
		return "redirect:/login";
	}

	@GetMapping("/user/home")
	public String userHomePage(Model model, HttpSession session, @Param("keyword") String keyword) {
		Long userId = (Long) session.getAttribute("userId");
		int totalQuantity = cartItemService.getTotalQuantityByUserId(userId);
		List<Product> listProduct;
		boolean isKeywordProvided = (keyword != null && !keyword.isEmpty());
		if (isKeywordProvided) {
			listProduct = productService.getAllProducts(keyword);
		} else {

			listProduct = productService.getAllProducts(null);
		}

		model.addAttribute("listProduct", listProduct);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalQuantity", totalQuantity);
		return "user_home";
	}

	@GetMapping("/user_registration_form")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "user_registration_form";
	}



	@GetMapping("/user/showProduct")
	public String userShowProduct(Model model, HttpSession session, @Param("keyword") String keyword) {
		Long userId = (Long) session.getAttribute("userId");
		int totalQuantity = cartItemService.getTotalQuantityByUserId(userId);

		List<Product> listProduct;
		boolean isKeywordProvided = (keyword != null && !keyword.isEmpty());
		if (isKeywordProvided) {
			listProduct = productService.getAllProducts(keyword);
		} else {

			listProduct = productService.getAllProducts(null);
		}

		model.addAttribute("listProduct", listProduct);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalQuantity", totalQuantity);
		return "user_show_product";
	}


	@GetMapping("/user/viewProductDetail/{productId}")
	public String userViewProduct(@PathVariable("productId") long productId, Model model, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");


		if (userId == null) {
			model.addAttribute("error", "User not logged in.");
			return "login"; 
		}


		Product product = productService.getProductById(productId);


		User user = userService.getUserById(userId);


		int totalQuantity = cartItemService.getTotalQuantityByUserId(userId);


		model.addAttribute("product", product);
		model.addAttribute("user", user);
		model.addAttribute("totalQuantity", totalQuantity);

		return "user_detail_product";
	}




	@GetMapping("/user/cart")
	public String viewCartItems(Model model, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		List<CartItem> cartItems = cartItemService.getCartItemsByUserId(userId);
		int totalQuantity = cartItemService.getTotalQuantityByUserId(userId);

		BigDecimal totalPrice = cartItems.stream()
				.map(cartItem -> cartItem.getProductDetail().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalPrice", totalPrice);

		return "user_cart_item"; 
	}

	@GetMapping("/deleteCart/{cartItemId}")
	public String deleteCartItem(@PathVariable(value = "cartItemId") long cartItemId) {
		this.cartItemService.deleteCartItemById(cartItemId);
		return "redirect:/user/cart";
	}



	@GetMapping("/user/checkout")
	public String userCheckout(Model model, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		List<CartItem> cartItems = cartItemService.getCartItemsByUserId(userId);

		BigDecimal totalPrice = cartItems.stream()
				.map(cartItem -> cartItem.getProductDetail().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		Order order = new Order();
		order.setTotalAmount(totalPrice); 
		int totalQuantity = cartItemService.getTotalQuantityByUserId(userId);



		model.addAttribute("totalQuantity", totalQuantity);

		model.addAttribute("order", order); 
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalPrice", totalPrice);

		return "user_checkout"; 
	}




	@PostMapping("/place")
	public String placeOrder(HttpSession session, 
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("email") String email,
			@RequestParam("phNo1") String phNo1,
			@RequestParam("phNo2") String phNo2,
			@RequestParam("address") String address,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute("userId");
		User user = userService.getUserById(userId);
		if (user == null) {

			return "redirect:/error";
		}

		Order order = orderService.placeOrder(user);
		order.setFirstName(firstName);
		order.setLastName(lastName);
		order.setEmail(email);
		order.setPhNo1(phNo1);
		order.setPhNo2(phNo2);
		order.setAddress(address);


		orderService.saveOrder(order);

		cartItemService.deleteCartItemsByUser(user);


		redirectAttributes.addFlashAttribute("orderMessage", "Order placed successfully. Order ID: " + order.getOrderId());

		return "redirect:/orderSuccess";
	}




	@GetMapping("/orderSuccess")
	public String userOrderSuccess(HttpSession session, Model model) {
		Long userId = (Long) session.getAttribute("userId");


		User user = userService.getUserById(userId);



		model.addAttribute("userName", user.getFirstName() + " " + user.getLastName());


		return "user_order_success";
	}



	@PostMapping("/userOrder")
	public String userOrder(@ModelAttribute("order") Order order, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");
		User user = userService.getUserById(userId);

		order.setUser(user);
		orderService.saveOrder(order);
		cartItemService.deleteCartItemsByUser(user);
		return "redirect:/user/home";
	}


	@GetMapping("/user/history")
	public String userHistory(Model model, HttpSession session, @Param("keyword") String keyword) {
		Long userId = (Long) session.getAttribute("userId");
		int totalQuantity = cartItemService.getTotalQuantityByUserId(userId);
		List<Product> listProduct;
		List<OrderItem> listOrderItem;


		listOrderItem = orderItemService.getOrderItemsByUserId(userId);

		boolean isKeywordProvided = (keyword != null && !keyword.isEmpty());
		if (isKeywordProvided) {
			listProduct = productService.getAllProducts(keyword);
		} else {

			listProduct = productService.getAllProducts(null);
		}

		model.addAttribute("listOrderItem", listOrderItem);
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalQuantity", totalQuantity);
		return "user_history";
	}

}
