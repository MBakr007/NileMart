package org.route.service;

import org.route.model.Category;
import org.route.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CategoryService {

    private static final String DUPLICATE_SLUG = "Slug already exists";
    private static final String INVALID_PARENT =
            "Parent category must be an existing top-level category";
    private static final Comparator<Category> BY_DISPLAY_ORDER = Comparator.comparing(
            Category::getDisplayOrder, Comparator.nullsLast(Integer::compareTo));

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryRow> getOrderedRows() {
        List<CategoryRow> rows = new ArrayList<>();
        for (Category parent : getTopLevelCategories()) {
            rows.add(toRow(parent, ""));
            getChildren(parent.getSlug()).stream()
                    .map(child -> toRow(child, parent.getName()))
                    .forEach(rows::add);
        }
        return rows;
    }

    public List<Category> getTopLevelCategories() {
        return findAll().stream()
                .filter(category -> isBlank(category.getParentSlug()))
                .sorted(BY_DISPLAY_ORDER)
                .toList();
    }

    public List<Category> getSubCategories() {
        return getTopLevelCategories().stream()
                .flatMap(parent -> getChildren(parent.getSlug()).stream())
                .toList();
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public Map<String, String> validate(Category category, Set<String> fieldsWithBeanErrors) {
        normalize(category);
        Set<String> skipped = fieldsWithBeanErrors == null ? Set.of() : fieldsWithBeanErrors;
        Map<String, String> errors = new LinkedHashMap<>();

        if (!skipped.contains("name") && hasDuplicateName(category)) {
            errors.put("name", duplicateNameMessage(category.getParentSlug()));
        }
        if (!skipped.contains("slug") && hasDuplicateSlug(category.getSlug())) {
            errors.put("slug", DUPLICATE_SLUG);
        }
        if (!skipped.contains("parentSlug") && !hasValidParent(category.getParentSlug())) {
            errors.put("parentSlug", INVALID_PARENT);
        }
        return errors;
    }

    public void save(Category category) {
        normalize(category);
        categoryRepository.save(category);
    }

    private List<Category> findAll() {
        return categoryRepository.findAll();
    }

    private List<Category> getChildren(String parentSlug) {
        return findAll().stream()
                .filter(category -> parentSlug.equals(normalizeSlug(category.getParentSlug())))
                .sorted(BY_DISPLAY_ORDER)
                .toList();
    }

    private static CategoryRow toRow(Category category, String parentName) {
        String name = isBlank(category.getParentSlug())
                ? category.getName()
                : "— " + category.getName();
        return new CategoryRow(name, category.getSlug(), parentName,
                category.getDisplayOrder(), category.isActive());
    }

    private boolean hasDuplicateName(Category candidate) {
        String name = candidate.getName();
        if (name == null) {
            return false;
        }
        String parentSlug = candidate.getParentSlug();
        return findAll().stream()
                .anyMatch(existing -> sameParent(existing.getParentSlug(), parentSlug)
                        && name.equalsIgnoreCase(existing.getName()));
    }

    private boolean hasDuplicateSlug(String slug) {
        return slug != null && categoryRepository.findBySlug(slug) != null;
    }

    private boolean hasValidParent(String parentSlug) {
        if (isBlank(parentSlug)) {
            return true;
        }
        Category parent = findBySlug(parentSlug);
        return parent != null && isBlank(parent.getParentSlug());
    }

    private String duplicateNameMessage(String parentSlug) {
        if (isBlank(parentSlug)) {
            return "A category with this name already exists under top level";
        }
        Category parent = findBySlug(parentSlug);
        String parentName = parent == null ? parentSlug : parent.getName();
        return "A category with this name already exists under " + parentName;
    }

    private static boolean sameParent(String first, String second) {
        return normalizeSlug(first).equals(normalizeSlug(second));
    }

    private static void normalize(Category category) {
        category.setParentSlug(normalizeSlug(category.getParentSlug()));
    }

    private static String normalizeSlug(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
