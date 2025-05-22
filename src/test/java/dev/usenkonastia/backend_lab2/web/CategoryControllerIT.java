package dev.usenkonastia.backend_lab2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.backend_lab2.dto.category.CategoryDto;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Category Controller Tests")
@Testcontainers
@TestPropertySource(properties = {
        "jwt.secret=testsecret"
})
public class CategoryControllerIT extends AbstractIT {

    private static final String DEFAULT_USER_EMAIL = "email@email.com";
    private static final String SECOND_USER_EMAIL = "user@email.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        reset(categoryService);
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Get public categories")
    void testGetPublicCategories() throws Exception {
        categoryRepository.save(CategoryEntity.builder()
                .user(getUserEntity(DEFAULT_USER_EMAIL))
                .categoryName("Public Category")
                .isPublic(true)
                .build());

        mockMvc.perform(get("/api/v1/category/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories[0].categoryName").value("Public Category"));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Get user categories")
    void testGetUserCategories() throws Exception {
        categoryRepository.save(CategoryEntity.builder()
                .categoryName("User Category")
                .user(getUserEntity(DEFAULT_USER_EMAIL))
                .isPublic(false)
                .build());

        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].categoryName").value("User Category"));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Create category")
    void testCreateCategory() throws Exception {
        getUserEntity(DEFAULT_USER_EMAIL);

        CategoryDto dto = CategoryDto.builder()
                .categoryName("New Category")
                .records(List.of())
                .isPublic(false)
                .build();

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("New Category"))
                .andExpect(jsonPath("$.isPublic").value(false));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Get category by ID")
    void testGetCategoryById() throws Exception {
        CategoryEntity saved = categoryRepository.save(CategoryEntity.builder()
                .user(getUserEntity(DEFAULT_USER_EMAIL))
                .categoryName("Test Category")
                .isPublic(false)
                .build());

        mockMvc.perform(get("/api/v1/category/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Test Category"));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Delete category by ID")
    void testDeleteCategory() throws Exception {
        CategoryEntity saved = categoryRepository.save(CategoryEntity.builder()
                .user(getUserEntity(DEFAULT_USER_EMAIL))
                .categoryName("Delete Me")
                .isPublic(false)
                .build());

        mockMvc.perform(delete("/api/v1/category/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertThat(categoryRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = SECOND_USER_EMAIL)
    @DisplayName("Delete other user's category by ID")
    void testDeleteCategoryForbidden() throws Exception {
        getUserEntity(SECOND_USER_EMAIL);
        CategoryEntity saved = categoryRepository.save(CategoryEntity.builder()
                .user(getUserEntity(DEFAULT_USER_EMAIL))
                .categoryName("Delete Me")
                .isPublic(false)
                .build());

        mockMvc.perform(delete("/api/v1/category/" + saved.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.detail").value("Access denied"));

        assertThat(categoryRepository.findById(saved.getId())).isPresent();
    }

    private UserEntity getUserEntity(String email) {
        return userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(UserEntity.builder()
                        .id(UUID.randomUUID())
                        .name("User")
                        .email(email)
                        .password("Ab@ut2025")
                        .build()));
    }
}
