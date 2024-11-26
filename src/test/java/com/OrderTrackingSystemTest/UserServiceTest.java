package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.repository.UserRepository;
import com.OrderTrackingSystem.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @DisplayName("JUnit test for getUserById method")
    @Test
    public void getUserByIdTest() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        User foundUser = userService.getUserById(1L);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(1L);
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }

    @DisplayName("JUnit test for getUserById method (User not found)")
    @Test
    public void getUserByIdNotFoundTest() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });
    }

    @DisplayName("JUnit test for findById method")
    @Test
    public void findByIdTest() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        User foundUser = userService.findById(1L);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(1L);
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }

    @DisplayName("JUnit test for findById method (User not found)")
    @Test
    public void findByIdNotFoundTest() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            userService.findById(1L);
        });
    }
}
