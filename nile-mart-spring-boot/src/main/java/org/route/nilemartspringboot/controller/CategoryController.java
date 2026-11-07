package org.route.nilemartspringboot.controller;

import org.route.nilemartspringboot.exception.CategoryNotFoundException;
import org.route.nilemartspringboot.model.Category;
import org.route.nilemartspringboot.service.CategoryService;
import org.route.nilemartspringboot.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable String id) throws CategoryNotFoundException {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public void saveCategory(@RequestBody Category category) {
        categoryService.saveCategory(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }
}
