package dev.usenkonastia.backend_lab2.service.impl;

import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.CategoryService;
import dev.usenkonastia.backend_lab2.service.exception.CategoryNotFoundException;
import dev.usenkonastia.backend_lab2.service.exception.ForbiddenException;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import dev.usenkonastia.backend_lab2.service.mapper.CategoryMapper;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Category addCategory(Category category) {
        UUID currentUserId = getCurrentUser();
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
        UUID currentUserId = getCurrentUser();
        return categoryMapper.toCategoryList(categoryRepository.findByUserId(currentUserId));
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        UUID ownerId = userRepository.findUserIdByCategoryId(id);
        if (ownerId == null) {
            return;
        }

        UUID currentUserId = getCurrentUser();
        if (!ownerId.equals(currentUserId)) {
            throw new ForbiddenException();
        }
        categoryRepository.deleteById(id);
    }

    private UUID getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .map(UserEntity::getId)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
