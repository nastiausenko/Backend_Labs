package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.security.AccessValidator;
import dev.usenkonastia.backend_lab2.security.SecurityContextService;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CategoryServiceImpl.class)
@DisplayName("Category Service Tests")
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private SecurityContextService securityContextService;

    @MockBean
    private AccessValidator accessValidator;

    @Autowired
    private CategoryService categoryService;

    private Category category;
    private CategoryEntity categoryEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
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
        when(securityContextService.getCurrentUserId()).thenReturn(userEntity.getId());
        when(categoryMapper.toCategoryEntity(any())).thenReturn(categoryEntity);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);
        when(categoryMapper.toCategory(categoryEntity)).thenReturn(category);

        Category newCategory = categoryService.addCategory(category);

        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getCategoryName()).isEqualTo("Category Name");

        verify(categoryRepository).save(any());
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toCategory(categoryEntity)).thenReturn(category);

        Category result = categoryService.getCategoryById(category.getId());

        assertThat(result).isNotNull();
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getCategoryName(), result.getCategoryName());
    }

    @Test
    void testGetCategoryById_NotFound() {
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
        when(securityContextService.getCurrentUserId()).thenReturn(userEntity.getId());
        when(categoryRepository.findByUserId(userEntity.getId())).thenReturn(List.of(categoryEntity));
        when(categoryMapper.toCategoryList(List.of(categoryEntity))).thenReturn(List.of(category));

        List<Category> result = categoryService.getUserCategories();

        assertThat(result).containsExactly(category);
    }

    @Test
    void testDeleteCategory_Success() {
        UUID categoryId = category.getId();
        UUID currentUserId = userEntity.getId();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(securityContextService.getCurrentUserId()).thenReturn(currentUserId);

        doNothing().when(accessValidator).validateOwner(currentUserId, userEntity.getId());

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void testDeleteCategory_NotExists() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatCode(() -> categoryService.deleteCategory(categoryId))
                .doesNotThrowAnyException();

        verify(categoryRepository, never()).deleteById(any());
        verifyNoInteractions(securityContextService);
        verifyNoInteractions(accessValidator);
    }

    @Test
    void testDeleteCategory_Forbidden() {
        UUID categoryId = category.getId();
        UUID currentUserId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(securityContextService.getCurrentUserId()).thenReturn(currentUserId);

        doThrow(new ForbiddenException()).when(accessValidator)
                .validateOwner(currentUserId, userEntity.getId());

        assertThatThrownBy(() -> categoryService.deleteCategory(categoryId))
                .isInstanceOf(ForbiddenException.class);

        verify(categoryRepository, never()).deleteById(any());
    }
}
