package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.*;
import com.OrderTrackingSystem.repository.CartItemRepository;
import com.OrderTrackingSystem.repository.OrderRepository;
import com.OrderTrackingSystem.repository.ProductDetailRepository;
import com.OrderTrackingSystem.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductDetailRepository productDetailRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private CartItem cartItem;
    private ProductDetail productDetail;
    private Order order;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");

        productDetail = new ProductDetail();
        productDetail.setProductDetailId(1L);
        productDetail.setStockQty(10);
        productDetail.setPrice(BigDecimal.valueOf(100));

        cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setProductDetail(productDetail);
        cartItem.setQuantity(2);
        cartItem.setUser(user);

        order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus("Processing");
        order.setTotalAmount(BigDecimal.valueOf(200));
    }

    @DisplayName("JUnit test for placeOrder method")
    @Test
    public void placeOrderTest() {
        List<CartItem> cartItems = List.of(cartItem);
        given(cartItemRepository.findByUser(user)).willReturn(cartItems);
        given(productDetailRepository.save(productDetail)).willReturn(productDetail);
        given(orderRepository.save(any(Order.class))).willReturn(order);

        Order placedOrder = orderService.placeOrder(user);

        assertThat(placedOrder).isNotNull();
        assertThat(placedOrder.getTotalAmount()).isEqualTo(BigDecimal.valueOf(200));
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
    }

    @DisplayName("JUnit test for calculateTotalPriceForToday method")
    @Test
    public void calculateTotalPriceForTodayTest() {
        given(orderRepository.findByOrderDate(LocalDate.now())).willReturn(List.of(order));

        BigDecimal totalPrice = orderService.calculateTotalPriceForToday(LocalDate.now());

        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(200));
    }

    @DisplayName("JUnit test for calculateTotalPriceForDate method")
    @Test
    public void calculateTotalPriceForDateTest() {
        given(orderRepository.sumTotalAmountByOrderDate(LocalDate.now())).willReturn(Optional.of(BigDecimal.valueOf(200)));
        BigDecimal totalPrice = orderService.calculateTotalPriceForDate(LocalDate.now());

        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(200));
    }

    @DisplayName("JUnit test for calculateTotalPriceForMonth method")
    @Test
    public void calculateTotalPriceForMonthTest() {
        YearMonth month = YearMonth.now();
        given(orderRepository.sumTotalAmountByOrderMonth(month.atDay(1), month.atEndOfMonth())).willReturn(
        		Optional.of(BigDecimal.valueOf(600)));

        BigDecimal totalPrice = orderService.calculateTotalPriceForMonth(month);
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(600));
    }

    @DisplayName("JUnit test for saveOrder method")
    @Test
    public void saveOrderTest() {
        given(orderRepository.save(order)).willReturn(order);

        Order savedOrder = orderService.saveOrder(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderId()).isEqualTo(1L);
        verify(orderRepository, times(1)).save(order);
    }
}
