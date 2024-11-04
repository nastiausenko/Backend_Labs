package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.entity.Category;
import dev.usenkonastia.backend_lab2.service.exception.CategoryNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CategoryService {
    private Map<UUID, Category> categories = new HashMap<>();

    public Category addCategory(Category category) {
        Category newCategory = Category.builder()
                .id(UUID.randomUUID())
                .categoryName(category.getCategoryName())
                .build();
        categories.put(newCategory.getId(), newCategory);
        return newCategory;
    }

    public Category getCategoryById(UUID id) {
        Category category = categories.get(id);
        if (category == null) {
            throw new CategoryNotFoundException(id);
        }
        return category;
    }

    public List<Category> getCategories() {
        return List.copyOf(categories.values());
    }

    public void deleteCategory(UUID id) {
        Category category = getCategoryById(id);
        categories.remove(category.getId());
    }
}
