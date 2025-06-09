package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.User;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.security.jwt.JwtUtil;
import dev.usenkonastia.backend_lab2.service.exception.EmailAlreadyExistsException;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import dev.usenkonastia.backend_lab2.service.impl.UserServiceImpl;
import dev.usenkonastia.backend_lab2.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("User Service Tests")
@SpringBootTest(classes = UserServiceImpl.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    private UUID userId;
    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .build();

        userEntity = UserEntity.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .build();
    }

    @Test
    void testRegisterUser() {
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userMapper.toUserEntity(any(User.class))).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUser(userEntity)).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("mocked-jwt");

        String token = userService.registerUser(user);

        assertThat(token).isEqualTo("mocked-jwt");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUser_AlreadyExists() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(userEntity));

        assertThatThrownBy(() -> userService.registerUser(user))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email test@example.com is already in use");

        verify(userRepository, never()).save(any());
    }

    @Test
    void testLoginUser() {
        String token = "mocked-token";

        when(jwtUtil.generateToken(user.getEmail())).thenReturn(token);

        String result = userService.loginUser(user);

        assertThat(result).isEqualTo(token);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(user.getEmail());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUser(userEntity)).thenReturn(user);

        User found = userService.getUserById(userId);

        assertThat(found).isEqualTo(user);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void testGetUsers() {
        List<UserEntity> entities = List.of(userEntity);
        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(entities);
        when(userMapper.toUserList(any())).thenReturn(users);

        List<User> result = userService.getUsers();

        assertThat(result).containsExactly(user);
    }

    @Test
    void testDeleteAccount() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test@example.com", null)
        );

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        userService.deleteAccount();

        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteAccount_UserNotFound() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("missing@example.com", null)
        );

        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.deleteAccount());
    }
}
