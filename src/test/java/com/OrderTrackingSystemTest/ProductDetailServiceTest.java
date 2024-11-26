package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.ProductDetail;
import com.OrderTrackingSystem.repository.ProductDetailRepository;
import com.OrderTrackingSystem.service.ProductDetailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductDetailServiceTest {

    @Mock
    private ProductDetailRepository productDetailRepository;

    @InjectMocks
    private ProductDetailService productDetailService;

    private ProductDetail productDetail;

    @BeforeEach
    public void setup() {
        productDetail = new ProductDetail();
        productDetail.setProductDetailId(1L);
        productDetail.setSizeNo(36.0);
        productDetail.setStockQty(50);
        productDetail.setPrice(BigDecimal.valueOf(100));
    }

    @DisplayName("JUnit test for saveProductDetail method")
    @Test
    public void saveProductDetailTest() {
        given(productDetailRepository.save(productDetail)).willReturn(productDetail);

        ProductDetail savedProductDetail = productDetailService.saveProductDetail(productDetail);

        assertThat(savedProductDetail).isNotNull();
        verify(productDetailRepository, times(1)).save(productDetail);
    }

    

    @DisplayName("JUnit test for deleteProductDetailById method")
    @Test
    public void deleteProductDetailByIdTest() {
        // Mock the behavior of deleteById
        doNothing().when(productDetailRepository).deleteById(1L);

        // Call the service method
        productDetailService.deleteProductDetailById(1L);

        // Verify that deleteById was called once
        verify(productDetailRepository, times(1)).deleteById(1L);
    }




	@DisplayName("JUnit test for getAllProductDetail method")
    @Test
    public void getAllProductDetailTest() {
        given(productDetailRepository.findAll()).willReturn(List.of(productDetail));

        List<ProductDetail> productDetails = productDetailService.getAllProductDetail();

        assertThat(productDetails).isNotNull();
        assertThat(productDetails.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for getAllProductDetails with keyword method")
    @Test
    public void getAllProductDetailsWithKeywordTest() {
        String keyword = "Sample";
        given(productDetailRepository.findByProductNameKeyword(keyword)).willReturn(List.of(productDetail));

        List<ProductDetail> productDetails = productDetailService.getAllProductDetails(keyword);

        assertThat(productDetails).isNotNull();
        assertThat(productDetails.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for getAllProductDetails with empty keyword method")
    @Test
    public void getAllProductDetailsWithEmptyKeywordTest() {
        given(productDetailRepository.findAll()).willReturn(List.of(productDetail));

        List<ProductDetail> productDetails = productDetailService.getAllProductDetails("");

        assertThat(productDetails).isNotNull();
        assertThat(productDetails.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for findById method")
    @Test
    public void findByIdTest() {
        given(productDetailRepository.findById(1L)).willReturn(Optional.of(productDetail));

        ProductDetail foundProductDetail = productDetailService.findById(1L);

        assertThat(foundProductDetail).isNotNull();
        assertThat(foundProductDetail.getProductDetailId()).isEqualTo(1L);
    }

    @DisplayName("JUnit test for findById method (not found)")
    @Test
    public void findByIdNotFoundTest() {
        given(productDetailRepository.findById(1L)).willReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            productDetailService.findById(1L);
        });
    }
}
