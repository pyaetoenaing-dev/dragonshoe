package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.OrderItem;
import com.OrderTrackingSystem.repository.OrderItemRepository;
import com.OrderTrackingSystem.service.OrderItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    private OrderItem orderItem;

    @BeforeEach
    public void setup() {
        orderItem = new OrderItem();
        orderItem.setOrderItemId(1L);
        orderItem.setQuantity(2);
    }

    @DisplayName("JUnit test for getOrderItemsByUserId method")
    @Test
    public void getOrderItemsByUserIdTest() {
        given(orderItemRepository.findByOrderUserId(1L)).willReturn(List.of(orderItem));

        List<OrderItem> orderItems = orderItemService.getOrderItemsByUserId(1L);

        assertThat(orderItems).isNotNull();
        assertThat(orderItems.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for getOrderItems method")
    @Test
    public void getOrderItemsTest() {
        given(orderItemRepository.findAll()).willReturn(List.of(orderItem));

        List<OrderItem> orderItems = orderItemService.getOrderItems();

        assertThat(orderItems).isNotNull();
        assertThat(orderItems.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for findOrderItemsByOrderDate method")
    @Test
    public void findOrderItemsByOrderDateTest() {
        LocalDate orderDate = LocalDate.of(2024, 11, 1);
        given(orderItemRepository.findByOrder_OrderDate(orderDate)).willReturn(List.of(orderItem));

        List<OrderItem> orderItems = orderItemService.findOrderItemsByOrderDate(orderDate);

        assertThat(orderItems).isNotNull();
        assertThat(orderItems.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for findByOrderDate method")
    @Test
    public void findByOrderDateTest() {
        LocalDate orderDate = LocalDate.of(2024, 11, 1);
        given(orderItemRepository.findByOrderOrderDate(orderDate)).willReturn(List.of(orderItem));

        List<OrderItem> orderItems = orderItemService.findByOrderDate(orderDate);

        assertThat(orderItems).isNotNull();
        assertThat(orderItems.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for findByOrderMonth method")
    @Test
    public void findByOrderMonthTest() {
        YearMonth month = YearMonth.of(2024, 11);
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        given(orderItemRepository.findByOrderDateBetween(startDate, endDate)).willReturn(List.of(orderItem));

        List<OrderItem> orderItems = orderItemService.findByOrderMonth(month);

        assertThat(orderItems).isNotNull();
        assertThat(orderItems.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for findOrderItemsByOrderMonth method")
    @Test
    public void findOrderItemsByOrderMonthTest() {
        YearMonth month = YearMonth.of(2024, 11);
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        given(orderItemRepository.findByOrderDateBetween(startDate, endDate)).willReturn(List.of(orderItem));

        List<OrderItem> orderItems = orderItemService.findOrderItemsByOrderMonth(month);

        assertThat(orderItems).isNotNull();
        assertThat(orderItems.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for findAll method")
    @Test
    public void findAllTest() {
        given(orderItemRepository.findAll()).willReturn(List.of(orderItem));

        List<OrderItem> orderItems = orderItemService.findAll();

        assertThat(orderItems).isNotNull();
        assertThat(orderItems.size()).isEqualTo(1);
    }
}
