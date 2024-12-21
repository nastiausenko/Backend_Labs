package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.dto.category.CategoryDto;
import dev.usenkonastia.backend_lab2.dto.category.CategoryListDto;
import dev.usenkonastia.backend_lab2.service.CategoryService;
import dev.usenkonastia.backend_lab2.service.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@Tag(name = "Category")
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/public")
    public ResponseEntity<CategoryListDto> getPublicCategories() {
        return ResponseEntity.ok(categoryMapper.toCategoryListDto(categoryService.getPublicCategories()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CategoryListDto> getUserCategories(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryMapper.toCategoryListDto(categoryService.getUserCategories(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.getCategoryById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto category) {
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.addCategory(categoryMapper.toCategory(category))));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
