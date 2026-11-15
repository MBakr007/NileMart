package org.route.service;

import org.route.model.Category;
import org.route.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private static final Comparator<Category> DISPLAY_ORDER = Comparator
            .comparing(Category::getDisplayOrder)
            .thenComparing(Category::getName, String.CASE_INSENSITIVE_ORDER);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getTopLevelCategories() {
        //getting the categories without a parent
        List<Category> parents = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            if (category.getParentSlug().isEmpty()) {
                parents.add(category);
            }
        }
        //ordering the parent categories
        parents.sort(DISPLAY_ORDER);
        return parents;
    }

    public List<Category> getActiveTopLevelCategories() {
        //getting only the active parent categories
        List<Category> parents = new ArrayList<>();
        for (Category category : getTopLevelCategories()) {
            if (category.isActive()) {
                parents.add(category);
            }
        }
        return parents;
    }

    public List<Category> getAvailableSubCategories() {
        //getting active sub-categories from active parents
        List<Category> available = new ArrayList<>();
        for (Category parent : getActiveTopLevelCategories()) {
            for (Category child : getSubCategories(parent.getSlug())) {
                if (child.isActive()) {
                    available.add(child);
                }
            }
        }
        return available;
    }

    public List<Map<String, Object>> getCategoriesForDisplay() {
        //adding each parent before its sub-categories
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Category parent : getTopLevelCategories()) {
            rows.add(Map.of("category", parent, "displayName", parent.getName(), "parentName", "—"));
            for (Category child : getSubCategories(parent.getSlug())) {
                rows.add(Map.of("category", child, "displayName", "— " + child.getName(),
                        "parentName", parent.getName()));
            }
        }
        return rows;
    }

    public boolean isAvailableSubCategory(String slug) {
        //checking if the category is an active sub-category
        Category category = categoryRepository.findBySlug(slug);
        if (category == null || !category.isActive() || category.getParentSlug().isEmpty()) {
            return false;
        }
        //checking if its parent is active and has no parent
        Category parent = categoryRepository.findBySlug(category.getParentSlug());
        return parent != null && parent.isActive() && parent.getParentSlug().isEmpty();
    }

    public String getCategoryName(String slug) {
        //getting the category name for the products table
        Category category = categoryRepository.findBySlug(slug);
        return category == null ? slug : category.getName();
    }

    public boolean saveCategory(Category category, Errors errors) {
        //using an empty parent slug for a top level category
        if (category.getParentSlug() == null) {
            category.setParentSlug("");
        }
        String parentSlug = category.getParentSlug();
        Category parent = categoryRepository.findBySlug(parentSlug);
        //checking if the parent exists and is a top level category
        if (!parentSlug.isEmpty()) {
            if (parent == null) {
                errors.rejectValue("parentSlug", "category.parent.missing", "Parent category does not exist");
            } else if (!parent.getParentSlug().isEmpty()) {
                errors.rejectValue("parentSlug", "category.parent.level", "Parent must be a top-level category");
            } else if (!parent.isActive()) {
                errors.rejectValue("parentSlug", "category.parent.inactive", "Parent category must be active");
            }
        }

        //checking the category name under the same parent
        if (!errors.hasFieldErrors("name") && !errors.hasFieldErrors("parentSlug")) {
            for (Category existing : categoryRepository.findAll()) {
                if (existing.getParentSlug().equals(parentSlug)
                        && existing.getName().equalsIgnoreCase(category.getName())) {
                    String location = parentSlug.isEmpty() ? "at the top level" : "under " + parent.getName();
                    errors.rejectValue("name", "category.name.duplicate",
                            "A category with this name already exists " + location);
                    break;
                }
            }
        }
        //checking if the slug is already used
        if (!errors.hasFieldErrors("slug") && categoryRepository.findBySlug(category.getSlug()) != null) {
            errors.rejectValue("slug", "category.slug.duplicate", "Slug already exists");
        }
        //returning without saving when there are errors
        if (errors.hasErrors()) {
            return false;
        }
        //saving the category
        categoryRepository.save(category);
        return true;
    }

    private List<Category> getSubCategories(String parentSlug) {
        //getting the sub-categories of this parent
        List<Category> children = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            if (parentSlug.equals(category.getParentSlug())) {
                children.add(category);
            }
        }
        //ordering the sub-categories
        children.sort(DISPLAY_ORDER);
        return children;
    }
}
