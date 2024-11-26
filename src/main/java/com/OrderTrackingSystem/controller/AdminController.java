package com.OrderTrackingSystem.controller;

import java.io.IOException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

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

import com.OrderTrackingSystem.FileUploadUtil;
import com.OrderTrackingSystem.model.Brand;
import com.OrderTrackingSystem.model.OrderItem;
import com.OrderTrackingSystem.model.Product;
import com.OrderTrackingSystem.model.ProductDetail;
import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.service.BrandService;
import com.OrderTrackingSystem.service.LoginService;
import com.OrderTrackingSystem.service.OrderItemService;
import com.OrderTrackingSystem.service.OrderService;
import com.OrderTrackingSystem.service.ProductDetailService;
import com.OrderTrackingSystem.service.ProductService;

@Controller
public class AdminController {
	
	@Autowired
    private LoginService loginService;
	
	@Autowired
    private ProductService productService;
	
	@Autowired
    private ProductDetailService productDetailService;
	
	@Autowired
    private OrderItemService orderItemService;
	
	@Autowired
    private BrandService brandService;
	
	@Autowired
    private OrderService orderService;
	
	@GetMapping("/registerAdmin")
    public String showAdminRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "adminRegistration";
    }
	
	@PostMapping("/registerAdmin")
    public String registerAdmin(@ModelAttribute("user") User user, Model model) {
        if (loginService.isUserRegistered(user.getEmail())) {
            model.addAttribute("error", "Admin with this email already registered.");
            return "adminRegistration";
        }
        loginService.registerAdmin(user);
        return "redirect:/login";
    }
    
	@GetMapping("/admin/productionManagerOrder")
	public String adminProductManagerOrder(@RequestParam(value = "selectedDate", required = false) LocalDate selectedDate, Model model) {
	    LocalDate today = LocalDate.now();
	    List<OrderItem> listOrderItem;
	    if (selectedDate != null) {
	        listOrderItem = orderItemService.findByOrderDate(selectedDate);
	    } else {
	        listOrderItem = orderItemService.findOrderItemsByOrderDate(today);
	        selectedDate = today;
	    }

	    
	    BigDecimal totalPrice = orderService.calculateTotalPriceForDate(selectedDate);

	    model.addAttribute("selectedDate", selectedDate);
	    model.addAttribute("totalPrice", totalPrice);
	    model.addAttribute("listOrderItem", listOrderItem);
	    return "admin_production_manager_order";
	}
	
	@GetMapping("/admin/productionManagerPA")
	public String adminProductManagerPA(Model model, @Param("keyword") String keyword) {
		List<ProductDetail> listProductDetail;
        boolean isKeywordProvided = (keyword != null && !keyword.isEmpty());
        if (isKeywordProvided) {
        	listProductDetail = productDetailService.getAllProductDetails(keyword);
        } else {
        	
        	listProductDetail = productDetailService.getAllProductDetails(null);
        }
        
        model.addAttribute("listProductDetail", listProductDetail);
        model.addAttribute("keyword", keyword);
	    return "admin_production_manager_pa";
	}

    
    @GetMapping("/admin/chiefAccountant")
    public String adminChiefAccountant(@RequestParam(value = "selectedMonth", required = false) YearMonth selectedMonth, Model model) {
    	YearMonth currentMonth = YearMonth.now();
        List<OrderItem> listOrderItem;
        if (selectedMonth != null) {
            listOrderItem = orderItemService.findByOrderMonth(selectedMonth);
        } else {
            listOrderItem = orderItemService.findOrderItemsByOrderMonth(currentMonth);
            selectedMonth = currentMonth;
        }       
        BigDecimal totalPrice = orderService.calculateTotalPriceForMonth(selectedMonth);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("listOrderItem", listOrderItem);
        return "admin_chief_accountant";
    }
    
    

    
    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
    	Product product = new Product();
        List<Brand> allBrands = brandService.getAllBrands();
        model.addAttribute("product", new Product());
        model.addAttribute("allBrands", allBrands); 
        return "clerk_add_product";
    }
    
    @GetMapping("/deleteProduct/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") long productId) {
        this.productService.deleteProductById(productId);
        return "redirect:/admin/clerk";
    }
    
    
    @GetMapping("/updateProduct/{productId}")
    public String updateProduct(@PathVariable(value = "productId") long productId, Model model) {
        Product product = productService.getProductById(productId);
        List<Brand> allBrands = brandService.getAllBrands();
        
        model.addAttribute("allBrands", allBrands);
        model.addAttribute("product", product);
        return "clerk_update_product";
    }
    
    @GetMapping("/viewProductDetail/{productId}")
    public String viewProductDetail(@PathVariable("productId") long productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "clerk_detail_product";
    }
    
    @RequestMapping("/admin/clerk")
    public String adminClerk(Model model, @Param("keyword") String keyword) {
        List<Product> listProduct;
        boolean isKeywordProvided = (keyword != null && !keyword.isEmpty());
        if (isKeywordProvided) {
        	listProduct = productService.getAllProducts(keyword);
        } else {
        	
        	listProduct = productService.getAllProducts(null);
        }
        
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("keyword", keyword);
        if (!isKeywordProvided) {
            findPaginated(1, "productId", "asc", model);
        }
        return "admin_clerk";
    } 
    
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("image") MultipartFile multipartFile,
                              @RequestParam("brand") Long brandId,
                              Model model) {
        try {
            
            Brand brand = brandService.findById(brandId);
            product.setBrand(brand);            
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setPhotos(fileName);
            Product savedProduct = productService.saveProduct(product);
            String uploadDir = "product-photos/" + savedProduct.getProductId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            return "redirect:/addProduct"; 
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error saving product: " + e.getMessage());
            return "clerk_add_product"; 
        }
    }
    
    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute("product") Product product,
                           @RequestParam("image") MultipartFile multipartFile,
                           
                           Model model) throws IOException {
    	try {
        	String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setPhotos(fileName);
            Product savedProduct = productService.saveProduct(product);
            String uploadDir = "product-photos/" + savedProduct.getProductId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            return "redirect:/admin/clerk"; 
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error saving product: " + e.getMessage());
            return "admin/clerk"; 
        }
    }
    
    @GetMapping("/addBrand")
    public String showAddBrandForm(Model model) {
    	List<Brand> listBrand;
    	Brand brand = new Brand();
    	listBrand = brandService.getAllBrands();
    	
    	model.addAttribute("brand", new Brand());
    	model.addAttribute("listBrand", listBrand);
        
        return "clerk_add_brand";
    }
    
    @PostMapping("/saveBrand")
    public String saveBrand(@ModelAttribute("brand") Brand brand) {
        
        Brand savedBrand = brandService.saveBrand(brand);
        return "redirect:/addBrand";
    }
    
    @PostMapping("/updateBrand")
    public String updateBrand(@ModelAttribute("updateBrand") Brand updatedBrand) {
        brandService.saveBrand(updatedBrand); // Save updated brand
        return "redirect:/addBrand"; 
    }   
    
    @GetMapping("/deleteBrand/{brandId}")
    public String deleteBrand(@PathVariable(value = "brandId") long brandId) {
        this.brandService.deleteBrandById(brandId);
        return "redirect:/addBrand";
    }
     
    @GetMapping("/addProductDetail")
    public String showAddProductDetailForm(Model model, @Param("keyword") String keyword) {
    	List<Product> listProduct;
    	List<ProductDetail> listProductDetail;
        ProductDetail productDetail = new ProductDetail();
        listProduct = productService.getProducts();
        listProductDetail = productDetailService.getAllProductDetail();
        
        boolean isKeywordProvided = (keyword != null && !keyword.isEmpty());
        if (isKeywordProvided) {
        	listProductDetail = productDetailService.getAllProductDetails(keyword);
        } else {
        	
        	listProductDetail = productDetailService.getAllProductDetails(null);
        }
        
        
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("listProductDetail", listProductDetail);
        model.addAttribute("keyword", keyword);
        model.addAttribute("productDetail", new ProductDetail());
        
        return "clerk_add_productdetail"; 
        
    }
    
    @GetMapping("/deleteProductDetail/{productDetailId}")
    public String deleteProuctDetail(@PathVariable(value = "productDetailId") long productDetailId) {
        this.productDetailService.deleteProductDetailById(productDetailId);
        return "redirect:/addProductDetail";
    }
    
    @PostMapping("/updateProductDetail")
    public String updateProductDetail(@ModelAttribute("updateProductDetail") ProductDetail updatedProductDetail) {
        productDetailService.saveProductDetail(updatedProductDetail); 
        return "redirect:/addProductDetail"; 
    }
    
    
    @PostMapping("/saveProductDetail")
    public String saveProductDetail(@ModelAttribute("productDetail") ProductDetail productDetail) {
        productDetailService.saveProductDetail(productDetail);
        return "redirect:/addProductDetail";
    }
	
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 6;

        Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Product> listProduct = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProduct", listProduct);
        return "admin_clerk";
    }   
}
