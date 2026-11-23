package org.route.nilemartspringboot.mapper.category;

import org.mapstruct.Mapper;
import org.route.nilemartspringboot.dto.category.AddCategoryRequest;
import org.route.nilemartspringboot.dto.category.CategoryResponse;
import org.route.nilemartspringboot.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);

    Category toCategory(AddCategoryRequest request);
}
