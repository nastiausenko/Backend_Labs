package dev.usenkonastia.backend_lab2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.backend_lab2.dto.record.RecordDto;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.RecordRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.RecordService;
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

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Testcontainers
@DisplayName("Record Controller Integration Tests")
@TestPropertySource(properties = {
        "jwt.secret=testsecret"
})
public class RecordControllerIT extends AbstractIT {

    private static final String DEFAULT_USER_EMAIL = "email@email.com";
    private static final String SECOND_USER_EMAIL = "user@email.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private RecordService recordService;

    private UserEntity defaultUser;
    private CategoryEntity defaultCategory;

    @BeforeEach
    void setUp() {
        reset(recordService);
        recordRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        defaultUser = saveUser(DEFAULT_USER_EMAIL);
        defaultCategory = saveCategory(defaultUser);
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Add new record")
    void testAddNewRecord() throws Exception {
        RecordDto dto = RecordDto.builder()
                .expense(100.0)
                .categoryId(defaultCategory.getId())
                .build();

        mockMvc.perform(post("/api/v1/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expense").value(100.0))
                .andExpect(jsonPath("$.categoryId").value(defaultCategory.getId().toString()));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Get record by ID")
    void testGetRecordById() throws Exception {
        RecordEntity saved = saveRecord(defaultUser, defaultCategory, 100.0);

        mockMvc.perform(get("/api/v1/record/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expense").value(100.0));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Delete record by ID")
    void testDeleteRecordById() throws Exception {
        RecordEntity saved = saveRecord(defaultUser, defaultCategory, 100.0);

        mockMvc.perform(delete("/api/v1/record/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertThat(recordRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = SECOND_USER_EMAIL)
    @DisplayName("Delete other user's record by ID")
    void testDeleteRecordForbidden() throws Exception {
        saveUser(SECOND_USER_EMAIL);
        RecordEntity saved = saveRecord(defaultUser, defaultCategory, 100.0);

        mockMvc.perform(delete("/api/v1/record/" + saved.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.detail").value("Access denied"));

        assertThat(recordRepository.findById(saved.getId())).isPresent();
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Get records by categoryId")
    void testGetRecordsByCategoryId() throws Exception {
        saveRecord(defaultUser, defaultCategory, 100.0);

        mockMvc.perform(get("/api/v1/record")
                        .param("categoryId", defaultCategory.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records[0].expense").value(100.0));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Get records by userId")
    void testGetRecordsByUserId() throws Exception {
        saveRecord(defaultUser, defaultCategory, 50.0);

        mockMvc.perform(get("/api/v1/record")
                        .param("userId", defaultUser.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records[0].expense").value(50.0));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER_EMAIL)
    @DisplayName("Get records by userId and categoryId")
    void testGetRecordsByUserIdAndCategoryId() throws Exception {
        saveRecord(defaultUser, defaultCategory, 50.0);

        mockMvc.perform(get("/api/v1/record")
                        .param("userId", defaultUser.getId().toString(),
                                "categoryId", defaultCategory.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records[0].expense").value(50.0));
    }

    private UserEntity saveUser(String email) {
        return userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(UserEntity.builder()
                        .id(UUID.randomUUID())
                        .name("User")
                        .email(email)
                        .password("Ab@ut2025")
                        .build()));
    }

    private CategoryEntity saveCategory(UserEntity user) {
        return categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .categoryName("Test Category")
                .user(user)
                .isPublic(false)
                .build());
    }

    private RecordEntity saveRecord(UserEntity user, CategoryEntity category, double expense) {
        return recordRepository.save(RecordEntity.builder()
                .id(UUID.randomUUID())
                .user(user)
                .category(category)
                .expense(expense)
                .date(ZonedDateTime.now())
                .build());
    }
}
