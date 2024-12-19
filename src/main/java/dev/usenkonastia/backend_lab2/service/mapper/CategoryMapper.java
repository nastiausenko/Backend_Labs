package dev.usenkonastia.backend_lab2.service.mapper;


import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.dto.category.CategoryDto;
import dev.usenkonastia.backend_lab2.dto.category.CategoryEntryDto;
import dev.usenkonastia.backend_lab2.dto.category.CategoryListDto;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "categoryName", target = "categoryName")
    @Mapping(source = "isPublic", target = "isPublic")
    @Mapping(source = "records", target = "records")
    Category toCategory(CategoryEntity categoryEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryName", target = "categoryName")
    @Mapping(source = "isPublic", target = "isPublic")
    @Mapping(source = "records", target = "records")
    Category toCategory(CategoryDto categoryDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "categoryName", target = "categoryName")
    @Mapping(source = "isPublic", target = "isPublic")
    @Mapping(source = "records", target = "records")
    CategoryEntity toCategoryEntity(Category category);

    @Mapping(source = "categoryName", target = "categoryName")
    @Mapping(source = "isPublic", target = "isPublic")
    @Mapping(source = "records", target = "records")
    CategoryDto toCategoryDto(Category category);

    default CategoryListDto toCategoryListDto(List<Category> categories) {
        return CategoryListDto.builder().categories(toCategoryEntry(categories)).build();
    }

    List<CategoryEntryDto> toCategoryEntry(List<Category> categories);

    List<Category> toCategoryList(List<CategoryEntity> byIsPublicTrue);
}
