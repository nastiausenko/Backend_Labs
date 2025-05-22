package dev.usenkonastia.backend_lab2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.backend_lab2.dto.user.request.LoginRequestDto;
import dev.usenkonastia.backend_lab2.dto.user.request.RegisterRequestDto;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Testcontainers
@DisplayName("User Controller Tests")
public class UserControllerIT extends AbstractIT {

    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "Pass1234!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        reset(userService);
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Register new user")
    void testRegisterUser() throws Exception {
        RegisterRequestDto request = RegisterRequestDto.builder()
                .email(EMAIL)
                .username("User")
                .password(PASSWORD)
                .build();

        mockMvc.perform(post("/api/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());

        assertThat(userRepository.findByEmail(EMAIL)).isPresent();
    }

    @Test
    @DisplayName("Login user")
    void testLoginUser() throws Exception {
        userRepository.save(UserEntity.builder()
                .id(UUID.randomUUID())
                .email(EMAIL)
                .name("Login Test")
                .password(passwordEncoder.encode(PASSWORD))
                .build());

        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("Login fails with bad credentials")
    void testLoginBadCredentials() throws Exception {
        userRepository.save(UserEntity.builder()
                .id(UUID.randomUUID())
                .email(EMAIL)
                .name("Bad Creds User")
                .password(passwordEncoder.encode(PASSWORD))
                .build());

        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .email(EMAIL)
                .password("Ab@ut2020")
                .build();

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Bad credentials"));
    }

    @Test
    @WithMockUser(username = EMAIL)
    @DisplayName("Get all users")
    void testGetAllUsers() throws Exception {
        userRepository.save(UserEntity.builder()
                .id(UUID.randomUUID())
                .email(EMAIL)
                .name("Test User")
                .password("Ab@ut2025")
                .build());

        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isArray());
    }

    @Test
    @DisplayName("Unauthorized access to protected endpoint")
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = EMAIL)
    @DisplayName("Get user by ID")
    void testGetUserById() throws Exception {
        UserEntity user = userRepository.save(UserEntity.builder()
                .id(UUID.randomUUID())
                .email(EMAIL)
                .name("Test User")
                .password("Ab@ut2025")
                .build());

        mockMvc.perform(get("/api/v1/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @WithMockUser(username = EMAIL)
    @DisplayName("Delete user account")
    void testDeleteAccount() throws Exception {
        userRepository.save(UserEntity.builder()
                .id(UUID.randomUUID())
                .email(EMAIL)
                .name("Test User")
                .password("Ab@ut2025")
                .build());

        mockMvc.perform(delete("/api/v1/user"))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findByEmail(EMAIL)).isEmpty();
    }
}
