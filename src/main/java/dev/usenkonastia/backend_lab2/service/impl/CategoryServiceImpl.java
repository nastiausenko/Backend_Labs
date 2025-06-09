package dev.usenkonastia.backend_lab2.service.impl;

import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.security.AccessValidator;
import dev.usenkonastia.backend_lab2.security.SecurityContextService;
import dev.usenkonastia.backend_lab2.service.CategoryService;
import dev.usenkonastia.backend_lab2.service.exception.CategoryNotFoundException;
import dev.usenkonastia.backend_lab2.service.mapper.CategoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SecurityContextService securityContextService;
    private final CategoryMapper categoryMapper;
    private final AccessValidator accessValidator;

    @Override
    @Transactional
    public Category addCategory(Category category) {
        UUID currentUserId = securityContextService.getCurrentUserId();

        category = Category.builder()
                .categoryName(category.getCategoryName())
                .userId(currentUserId)
                .isPublic(category.getIsPublic())
                .records(category.getRecords())
                .build();
        return categoryMapper.toCategory(categoryRepository.save(categoryMapper.toCategoryEntity(category)));

    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(UUID id) {
        return categoryMapper.toCategory(categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getPublicCategories() {
        return categoryMapper.toCategoryList(categoryRepository.findByIsPublicTrue());

    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getUserCategories() {
        UUID currentUserId = securityContextService.getCurrentUserId();
        return categoryMapper.toCategoryList(categoryRepository.findByUserId(currentUserId));
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        categoryRepository.findById(id).ifPresent(categoryEntity -> {
            UUID ownerId = categoryEntity.getUser().getId();
            UUID currentUserId = securityContextService.getCurrentUserId();
            accessValidator.validateOwner(currentUserId, ownerId);
            categoryRepository.deleteById(id);
        });
    }

    public void validateCategoryExists(UUID categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
