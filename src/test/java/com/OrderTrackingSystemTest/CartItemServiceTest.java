package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.CartItem;
import com.OrderTrackingSystem.model.ProductDetail;
import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.repository.CartItemRepository;
import com.OrderTrackingSystem.service.CartItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    private CartItem cartItem;
    private User user;
    private ProductDetail productDetail;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUserId(1L);

        productDetail = new ProductDetail();
        productDetail.setProductDetailId(1L);

        cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setUser(user);
        cartItem.setProductDetail(productDetail);
        cartItem.setQuantity(3);
    }

    @DisplayName("JUnit test for saveCartItem method")
    @Test
    public void saveCartItemTest() {
        given(cartItemRepository.save(cartItem)).willReturn(cartItem);

        CartItem savedCartItem = cartItemService.saveCartItem(cartItem);

        assertThat(savedCartItem).isNotNull();
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @DisplayName("JUnit test for findCartItemById method")
    @Test
    public void findCartItemByIdTest() {
        given(cartItemRepository.findById(1L)).willReturn(Optional.of(cartItem));

        CartItem foundCartItem = cartItemService.findCartItemById(1L);

        assertThat(foundCartItem).isNotNull();
        assertThat(foundCartItem.getCartItemId()).isEqualTo(1L);
    }

    @DisplayName("JUnit test for findCartItemById method (not found)")
    @Test
    public void findCartItemByIdNotFoundTest() {
        given(cartItemRepository.findById(1L)).willReturn(Optional.empty());

        CartItem foundCartItem = cartItemService.findCartItemById(1L);

        assertThat(foundCartItem).isNull();
    }

    @DisplayName("JUnit test for getCartItemsByUserId method")
    @Test
    public void getCartItemsByUserIdTest() {
        given(cartItemRepository.findByUserUserId(1L)).willReturn(List.of(cartItem));

        List<CartItem> cartItems = cartItemService.getCartItemsByUserId(1L);

        assertThat(cartItems).isNotNull();
        assertThat(cartItems.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for getTotalQuantityByUserId method")
    @Test
    public void getTotalQuantityByUserIdTest() {
        given(cartItemRepository.sumQuantityByUserId(1L)).willReturn(5);

        int totalQuantity = cartItemService.getTotalQuantityByUserId(1L);

        assertThat(totalQuantity).isEqualTo(5);
    }

    @DisplayName("JUnit test for getTotalQuantityByUserId method (null value)")
    @Test
    public void getTotalQuantityByUserIdNullTest() {
        given(cartItemRepository.sumQuantityByUserId(1L)).willReturn(null);

        int totalQuantity = cartItemService.getTotalQuantityByUserId(1L);

        assertThat(totalQuantity).isEqualTo(0);
    }

    @DisplayName("JUnit test for deleteCartItemsByUser method")
    @Test
    public void deleteCartItemsByUserTest() {
        willDoNothing().given(cartItemRepository).deleteByUser(user);

        cartItemService.deleteCartItemsByUser(user);

        verify(cartItemRepository, times(1)).deleteByUser(user);
    }

    @DisplayName("JUnit test for deleteCartItemById method")
    @Test
    public void deleteCartItemByIdTest() {
        willDoNothing().given(cartItemRepository).deleteById(1L);

        cartItemService.deleteCartItemById(1L);

        verify(cartItemRepository, times(1)).deleteById(1L);
    }

    @DisplayName("JUnit test for findCartItemByUserAndProductDetail method")
    @Test
    public void findCartItemByUserAndProductDetailTest() {
        given(cartItemRepository.findByUserAndProductDetail(user, productDetail)).willReturn(cartItem);

        CartItem foundCartItem = cartItemService.findCartItemByUserAndProductDetail(user, productDetail);

        assertThat(foundCartItem).isNotNull();
        assertThat(foundCartItem.getProductDetail()).isEqualTo(productDetail);
    }
}
