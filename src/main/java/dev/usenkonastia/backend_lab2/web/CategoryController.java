package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryEntity category) {
        return ResponseEntity.ok(categoryService.addCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryEntity> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
