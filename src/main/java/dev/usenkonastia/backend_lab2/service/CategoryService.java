package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.exception.CategoryNotFoundException;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import dev.usenkonastia.backend_lab2.service.mapper.CategoryMapper;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
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
    public List<Category> getUserCategories(UUID userId) {
        try {
            userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            return categoryMapper.toCategoryList(categoryRepository.findByUserId(userId));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional
    public void deleteCategory(UUID id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
