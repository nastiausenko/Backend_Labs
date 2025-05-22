package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category addCategory(Category category);
    Category getCategoryById(UUID id);
    List<Category> getPublicCategories();
    List<Category> getUserCategories();
    void deleteCategory(UUID id);
}
