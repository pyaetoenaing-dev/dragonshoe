package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.Brand;

import com.OrderTrackingSystem.repository.BrandRepository;
import com.OrderTrackingSystem.service.BrandService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    private Brand brand;

    @BeforeEach
    public void setup() {
        brand = new Brand();
        brand.setBrandId(1L);
        brand.setBrandName("Nike");
    }

    @DisplayName("JUnit test for saveBrand method")
    @Test
    public void saveBrandTest() {
        given(brandRepository.save(brand)).willReturn(brand);

        Brand savedBrand = brandService.saveBrand(brand);

        assertThat(savedBrand).isNotNull();
        verify(brandRepository, times(1)).save(brand);
    }

    @DisplayName("JUnit test for getAllBrands method")
    @Test
    public void getAllBrandsTest() {
        Brand brand2 = new Brand();
        brand2.setBrandId(2L);
        brand2.setBrandName("Adidas");

        given(brandRepository.findAll()).willReturn(List.of(brand, brand2));

        List<Brand> brandList = brandService.getAllBrands();

        assertThat(brandList).isNotNull();
        assertThat(brandList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getAllBrands method (empty list)")
    @Test
    public void getAllBrandsEmptyTest() {
        given(brandRepository.findAll()).willReturn(Collections.emptyList());

        List<Brand> brandList = brandService.getAllBrands();

        assertThat(brandList).isEmpty();
        assertThat(brandList.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for getBrandById method")
    @Test
    public void getBrandByIdTest() {
        given(brandRepository.findById(1L)).willReturn(Optional.of(brand));
        Brand foundBrand = brandService.getBrandById(1L);
        assertThat(foundBrand).isNotNull();
    }

    @DisplayName("JUnit test for getBrandById method (Brand not found)")
    @Test
    public void getBrandByIdNotFoundTest() {
        given(brandRepository.findById(1L)).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            brandService.getBrandById(1L);
        });
    }

    @DisplayName("JUnit test for deleteBrandById method")
    @Test
    public void deleteBrandByIdTest() {
        willDoNothing().given(brandRepository).deleteById(1L);
        brandService.deleteBrandById(1L);
        verify(brandRepository, times(1)).deleteById(1L);
    }

    @DisplayName("JUnit test for findByIds method")
    @Test
    public void findByIdsTest() {
        Brand brand2 = new Brand();
        brand2.setBrandId(2L);
        brand2.setBrandName("Adidas");

        given(brandRepository.findAllById(List.of(1L, 2L))).willReturn(List.of(brand, brand2));

        List<Brand> brands = brandService.findByIds(List.of(1L, 2L));

        assertThat(brands).isNotNull();
        assertThat(brands.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for findById method")
    @Test
    public void findByIdTest() {
        given(brandRepository.findById(1L)).willReturn(Optional.of(brand));

        Brand foundBrand = brandService.findById(1L);

        assertThat(foundBrand).isNotNull();
        assertThat(foundBrand.getBrandName()).isEqualTo("Nike");
    }

    @DisplayName("JUnit test for findById method (Brand not found)")
    @Test
    public void findByIdNotFoundTest() {
        given(brandRepository.findById(1L)).willReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            brandService.findById(1L);
        });
    }
}
