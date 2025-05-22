package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.exception.CategoryNotFoundException;
import dev.usenkonastia.backend_lab2.service.exception.ForbiddenException;
import dev.usenkonastia.backend_lab2.service.impl.CategoryServiceImpl;
import dev.usenkonastia.backend_lab2.service.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Category Service Tests")
@SpringBootTest(classes = CategoryServiceImpl.class)
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    private Category category;
    private CategoryEntity categoryEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("email@example.com", null, List.of())
        );

        userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("user")
                .email("email@example.com")
                .password("Ab@ut2025")
                .build();

        category = Category.builder()
                .id(UUID.randomUUID())
                .userId(userEntity.getId())
                .categoryName("Category Name")
                .isPublic(false)
                .records(null)
                .build();

        categoryEntity = CategoryEntity.builder()
                .categoryName("Category Name")
                .isPublic(false)
                .records(null)
                .user(userEntity)
                .build();
    }

    @Test
    void testAddCategory() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        when(categoryMapper.toCategoryEntity(any(Category.class))).thenReturn(categoryEntity);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        when(categoryMapper.toCategory(any(CategoryEntity.class))).thenReturn(category);

        Category newCategory = categoryService.addCategory(category);

        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getCategoryName()).isEqualTo("Category Name");

        verify(categoryRepository).save(any(CategoryEntity.class));
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toCategory(any(CategoryEntity.class))).thenReturn(category);
        Category result = categoryService.getCategoryById(category.getId());

        assertThat(result).isNotNull();
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getCategoryName(), result.getCategoryName());
    }

    @Test
    void testGetCategoryByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(id))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void testGetPublicCategories() {
        when(categoryRepository.findByIsPublicTrue()).thenReturn(List.of(categoryEntity));
        when(categoryMapper.toCategoryList(List.of(categoryEntity))).thenReturn(List.of(category));

        List<Category> result = categoryService.getPublicCategories();

        assertThat(result).containsExactly(category);
    }

    @Test
    void testGetUserCategories() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        when(categoryRepository.findByUserId(userEntity.getId())).thenReturn(List.of(categoryEntity));
        when(categoryMapper.toCategoryList(List.of(categoryEntity))).thenReturn(List.of(category));

        List<Category> result = categoryService.getUserCategories();

        assertThat(result).containsExactly(category);
    }

    @Test
    void testDeleteCategory() {
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(userEntity));

        UUID id = category.getId();

        when(categoryRepository.existsById(id)).thenReturn(true);
        when(userRepository.findUserIdByCategoryId(id)).thenReturn(userEntity.getId());

        categoryService.deleteCategory(id);

        verify(categoryRepository).deleteById(id);
    }

    @Test
    void testDeleteCategoryNotExist() {
        when(categoryRepository.existsById(any())).thenReturn(false);
        assertDoesNotThrow(() -> categoryService.deleteCategory(category.getId()));

        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteCategoryForbidden() {
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(userEntity));

        UUID id = category.getId();
        when(categoryRepository.existsById(id)).thenReturn(true);
        when(userRepository.findUserIdByCategoryId(id)).thenReturn(UUID.randomUUID());

        assertThatThrownBy(() -> categoryService.deleteCategory(id))
                .isInstanceOf(ForbiddenException.class);

        verify(categoryRepository, never()).deleteById(id);
    }
}
