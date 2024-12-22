package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.exception.CategoryNotFoundException;
import dev.usenkonastia.backend_lab2.service.exception.ForbiddenException;
import dev.usenkonastia.backend_lab2.service.mapper.CategoryMapper;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public Category addCategory(Category category) {
        try {
            UUID currentUserId = getCurrentUser();
            category = Category.builder()
                    .categoryName(category.getCategoryName())
                    .userId(currentUserId)
                    .isPublic(category.getIsPublic())
                    .records(category.getRecords())
                    .build();
            return categoryMapper.toCategory(categoryRepository.save(categoryMapper.toCategoryEntity(category)));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(UUID id) {
        return categoryMapper.toCategory(categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
    }


    @Transactional(readOnly = true)
    public List<Category> getPublicCategories() {
        try {
            return categoryMapper.toCategoryList(categoryRepository.findByIsPublicTrue());
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Category> getUserCategories() {
        try {
            UUID currentUserId = getCurrentUser();
            return categoryMapper.toCategoryList(categoryRepository.findByUserId(currentUserId));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional
    public void deleteCategory(UUID id) {
        try {
            boolean categoryExists = categoryRepository.existsById(id);
            if (!categoryExists) {
                return;
            }
            if (!doesHaveRights(id)) {
                throw new ForbiddenException();
            }
            categoryRepository.deleteById(id);
        } catch (ForbiddenException e) {
            throw new ForbiddenException();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    private boolean doesHaveRights(UUID id) {
        UUID currentUserId = getCurrentUser();
        UUID userId = userRepository.findUserIdByCategoryId(id);
        if (userId == null) {
            throw new CategoryNotFoundException(id);
        }
        return userId.equals(currentUserId);
    }

    private UUID getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).get().getId();
    }
}
