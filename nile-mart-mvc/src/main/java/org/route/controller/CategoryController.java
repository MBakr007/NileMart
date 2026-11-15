package org.route.controller;

import jakarta.validation.Valid;
import org.route.model.Category;
import org.route.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("parentCategories")
    public List<Category> parentCategories() {
        //getting the parent categories for the form
        return categoryService.getActiveTopLevelCategories();
    }

    @GetMapping("/categories")
    public String getCategories(Model model) {
        //getting the ordered categories for the table
        model.addAttribute("categories", categoryService.getCategoriesForDisplay());
        return "categories";
    }

    @GetMapping("/categories/new")
    public String showForm(Model model) {
        //creating an empty category for the form
        model.addAttribute("category", new Category());
        return "categoryForm";
    }

    @PostMapping("/categories")
    public String addCategory(@ModelAttribute("category") @Valid Category category, BindingResult result) {
        //saving the category through the service
        if (!categoryService.saveCategory(category, result)) {
            //showing the same form with the errors
            return "categoryForm";
        }
        //redirecting after saving the category
        return "redirect:/categories";
    }
}
