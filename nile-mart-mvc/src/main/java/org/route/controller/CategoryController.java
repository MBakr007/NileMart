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

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getOrderedRows());
        return "categories";
    }

    @GetMapping("/categories/new")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", Category.builder().build());
        addFormOptions(model);
        return "categoryForm";
    }

    @PostMapping("/categories")
    public String saveCategory(@ModelAttribute("category") @Valid Category category,
                               BindingResult result, Model model) {
        FormErrors.rejectAll(result,
                categoryService.validate(category, FormErrors.fieldsWithErrors(result)));
        if (result.hasErrors()) {
            addFormOptions(model);
            return "categoryForm";
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    private void addFormOptions(Model model) {
        model.addAttribute("parentCategories", categoryService.getTopLevelCategories());
    }
}
