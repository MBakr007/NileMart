package org.route.nilemartspringboot.controller;

import org.route.nilemartspringboot.dto.category.AddCategoryRequest;
import org.route.nilemartspringboot.dto.category.CategoryResponse;
import org.route.nilemartspringboot.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public CategoryResponse getCategoryById(@PathVariable String id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/categories")
    public void saveCategory(@RequestBody AddCategoryRequest request) {
        categoryService.saveCategory(request);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategoryById(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }
}
