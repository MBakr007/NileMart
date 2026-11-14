package org.route.nilemartspringboot.service;


import org.route.nilemartspringboot.dto.category.AddCategoryRequest;
import org.route.nilemartspringboot.dto.category.CategoryResponse;
import org.route.nilemartspringboot.mapper.CategoryMapper;
import org.route.nilemartspringboot.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(String id) {
        return categoryMapper.toCategoryResponse(categoryRepository.findById(id));
    }

    public void saveCategory(AddCategoryRequest request) {
        categoryRepository.save(categoryMapper.toCategory(request));
    }

    public void deleteCategory(String id) {
        categoryRepository.delete(id);
    }
}
