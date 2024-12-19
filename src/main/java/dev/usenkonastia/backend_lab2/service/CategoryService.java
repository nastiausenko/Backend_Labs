package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.exception.CategoryNotFoundException;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryEntity addCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public CategoryEntity getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<CategoryEntity> getPublicCategories() {
        return categoryRepository.findByIsPublicTrue();
    }

    public List<CategoryEntity> getUserCategories(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return categoryRepository.findByUserId(userId);
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}
