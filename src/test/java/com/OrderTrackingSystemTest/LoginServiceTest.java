package com.OrderTrackingSystemTest;

import com.OrderTrackingSystem.model.Role;
import com.OrderTrackingSystem.model.User;
import com.OrderTrackingSystem.repository.RoleRepository;
import com.OrderTrackingSystem.repository.UserRepository;
import com.OrderTrackingSystem.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginService loginService;

    private User user;
    private Role role;

    @BeforeEach
    public void setup() {
        role = new Role();
        role.setRoleName("USER");
        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(role);
        user.setAccStatus("User");
    }

    @DisplayName("JUnit test for authenticate method")
    @Test
    public void authenticateTest() {
        given(userRepository.findByEmail("test@example.com")).willReturn(user);

        User authenticatedUser = loginService.authenticate("test@example.com", "password");

        assertThat(authenticatedUser).isNotNull();
    }

    @DisplayName("JUnit test for authenticate method (failure scenario)")
    @Test
    public void authenticateFailureTest() {
        given(userRepository.findByEmail("test@example.com")).willReturn(user);

        User authenticatedUser = loginService.authenticate("test@example.com", "wrongpassword");

        assertThat(authenticatedUser).isNull();
    }

    @DisplayName("JUnit test for isUserRegistered method")
    @Test
    public void isUserRegisteredTest() {
        given(userRepository.findByEmail("test@example.com")).willReturn(user);

        boolean isRegistered = loginService.isUserRegistered("test@example.com");

        assertThat(isRegistered).isTrue();
    }

    @DisplayName("JUnit test for getUserById method")
    @Test
    public void getUserByIdTest() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        User foundUser = loginService.getUserById(1L);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(1L);
    }

    @DisplayName("JUnit test for getUserById method (user not found)")
    @Test
    public void getUserByIdNotFoundTest() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        User foundUser = loginService.getUserById(1L);

        assertThat(foundUser).isNull();
    }

    @DisplayName("JUnit test for findByEmail method")
    @Test
    public void findByEmailTest() {
        given(userRepository.findByEmail("test@example.com")).willReturn(user);

        User foundUser = loginService.findByEmail("test@example.com");

        assertThat(foundUser).isNotNull();
    }

    @DisplayName("JUnit test for getUsers method")
    @Test
    public void getUsersTest() {
        User user2 = new User();
        user2.setUserId(2L);
        user2.setEmail("user2@example.com");

        given(userRepository.findAll()).willReturn(List.of(user, user2));

        List<User> userList = loginService.getUsers();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getLoggedInUser method")
    @Test
    public void getLoggedInUserTest() {
        given(request.getSession(false)).willReturn(session);
        given(session.getAttribute("loggedInUser")).willReturn(user);

        User loggedInUser = loginService.getLoggedInUser(request);

        assertThat(loggedInUser).isNotNull();
    }

    @DisplayName("JUnit test for isEmailExists method")
    @Test
    public void isEmailExistsTest() {
        given(userRepository.findByEmail("test@example.com")).willReturn(user);

        boolean emailExists = loginService.isEmailExists("test@example.com");

        assertThat(emailExists).isTrue();
    }

    @DisplayName("JUnit test for isEmailExists method (email not found)")
    @Test
    public void isEmailExistsNotFoundTest() {
        given(userRepository.findByEmail("test@example.com")).willReturn(null);

        boolean emailExists = loginService.isEmailExists("test@example.com");

        assertThat(emailExists).isFalse();
    }
}
