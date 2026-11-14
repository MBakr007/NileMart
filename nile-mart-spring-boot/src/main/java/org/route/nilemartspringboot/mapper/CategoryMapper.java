package org.route.nilemartspringboot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.route.nilemartspringboot.dto.category.AddCategoryRequest;
import org.route.nilemartspringboot.dto.category.CategoryResponse;
import org.route.nilemartspringboot.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "active", target = "isActive")
    CategoryResponse toCategoryResponse(Category category);

    Category toCategory(AddCategoryRequest request);
}
