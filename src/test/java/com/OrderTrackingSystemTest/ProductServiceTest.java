package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.Product;
import com.OrderTrackingSystem.repository.ProductDetailRepository;
import com.OrderTrackingSystem.repository.ProductRepository;
import com.OrderTrackingSystem.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductDetailRepository productDetailRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setup() {
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setPhotos("test-path.jpg");
    }

    @DisplayName("JUnit test for saveProduct method")
    @Test
    public void saveProductTest() {
        given(productRepository.save(product)).willReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertThat(savedProduct).isNotNull();
        verify(productRepository, times(1)).save(product);
    }

    @DisplayName("JUnit test for findById method")
    @Test
    public void findByIdTest() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        Product foundProduct = productService.findById(1L);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getProductId()).isEqualTo(1L);
    }

    @DisplayName("JUnit test for findById method (not found scenario)")
    @Test
    public void findByIdNotFoundTest() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            productService.findById(1L);
        });
    }

    @DisplayName("JUnit test for getAllProducts method with keyword")
    @Test
    public void getAllProductsWithKeywordTest() {
        String keyword = "Test";
        given(productRepository.findByKeyword(keyword)).willReturn(List.of(product));
        List<Product> products = productService.getAllProducts(keyword);
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for getAllProducts method without keyword")
    @Test
    public void getAllProductsWithoutKeywordTest() {
        given(productRepository.findAll()).willReturn(List.of(product));
        List<Product> products = productService.getAllProducts(null);
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for deleteProductById method")
    @Test
    public void deleteProductByIdTest() {
        doNothing().when(productRepository).deleteById(1L);
        productService.deleteProductById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @DisplayName("JUnit test for findPaginated method")
    @Test
    public void findPaginatedTest() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("productName").ascending());
        Page<Product> productPage = new PageImpl<>(List.of(product));
        given(productRepository.findAll(pageable)).willReturn(productPage);
        Page<Product> paginatedProducts = productService.findPaginated(1, 5, "productName", "ASC");
        assertThat(paginatedProducts).isNotNull();
        assertThat(paginatedProducts.getContent().size()).isEqualTo(1);
    }
}
