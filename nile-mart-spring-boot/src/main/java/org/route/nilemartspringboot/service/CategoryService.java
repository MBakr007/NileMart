package org.route.nilemartspringboot.service;


import lombok.extern.slf4j.Slf4j;
import org.route.nilemartspringboot.dto.PageResponse;
import org.route.nilemartspringboot.dto.category.AddCategoryRequest;
import org.route.nilemartspringboot.dto.category.CategoryResponse;
import org.route.nilemartspringboot.exception.CategoryException;
import org.route.nilemartspringboot.mapper.category.CategoryMapper;
import org.route.nilemartspringboot.model.Category;
import org.route.nilemartspringboot.repository.category.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Finds one page of categories. The categories are filtered by active, then sorted, then cut
     * down to the requested page.
     *
     * @param size      the number of categories per page, at least 1
     * @param page      the page number, starting from zero
     * @param sort      the field to sort by: {@code name} or {@code slug}
     * @param direction the sort direction: {@code asc} or {@code desc}
     * @param active    the active value to filter by, or {@code null} for all categories
     * @return the requested page, with empty content if it is past the last page
     * @throws CategoryException with {@link HttpStatus#BAD_REQUEST} if the sort field or direction is not supported
     */
    public PageResponse<CategoryResponse> getAllCategories(int size, int page, String sort, String direction, Boolean active) {
        log.info("start getting categories with size: {}, page: {}, sort: {}, direction: {}, active: {}",
                size, page, sort, direction, active);

        Comparator<Category> comparator = getComparator(sort, direction);

        List<Category> categories = new ArrayList<>();
        for (Category c : categoryRepository.findAll()) {
            if (active == null || active.equals(c.getActive()))
                categories.add(c);
        }
        categories.sort(comparator);

        int totalElements = categories.size();
        int totalPages = totalElements / size;
        if (totalElements % size != 0) {
            totalPages++;
        }

        // long avoids an int overflow when the page number is very large
        long start = Math.min((long) page * size, totalElements);
        long end = Math.min(start + size, totalElements);

        List<CategoryResponse> content = new ArrayList<>();
        for (int i = (int) start; i < end; i++) {
            content.add(categoryMapper.toCategoryResponse(categories.get(i)));
        }

        PageResponse<CategoryResponse> pageResponse = new PageResponse<>();
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setContent(content);
        pageResponse.setTotalElements(totalElements);
        pageResponse.setTotalPages(totalPages);

        log.info("returning {} of {} categories", content.size(), totalElements);
        return pageResponse;
    }

    /**
     * Finds a category by its id.
     *
     * @param id the category id
     * @return the matching category
     * @throws CategoryException with {@link HttpStatus#NOT_FOUND} if the category does not exist
     */
    public CategoryResponse getCategoryById(Integer id) {
        log.info("start getting category with id: {}", id);
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(() -> notFound(id));
    }

    /**
     * Saves a new category.
     *
     * @param request the category to save
     * @return the saved category, including its generated id
     */
    public CategoryResponse saveCategory(AddCategoryRequest request) {
        log.info("start saving category with name: {}", request.getName());
        Category savedCategory = categoryRepository.save(categoryMapper.toCategory(request));
        log.info("saved category with id: {}", savedCategory.getId());
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    /**
     * Deletes a category by its id.
     *
     * @param id the category id
     * @throws CategoryException with {@link HttpStatus#NOT_FOUND} if the category does not exist
     */
    public void deleteCategory(Integer id) {
        log.info("start deleting category with id: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw notFound(id);
        }
        categoryRepository.deleteById(id);
        log.info("deleted category with id: {}", id);
    }

    /**
     * Logs and builds the exception for a category that does not exist.
     *
     * @param id the category id
     * @return the exception to throw
     */
    private CategoryException notFound(Integer id) {
        log.warn("category with id: {} not found", id);
        return new CategoryException("Category with id: " + id + " not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Builds the comparator used to sort categories. Categories with equal values are ordered by id,
     * so a category never moves between pages.
     *
     * @param sort      the field to sort by: {@code name} or {@code slug}
     * @param direction the sort direction: {@code asc} or {@code desc}
     * @return the comparator
     * @throws CategoryException with {@link HttpStatus#BAD_REQUEST} if the sort field or direction is not supported
     */
    private Comparator<Category> getComparator(String sort, String direction) {
        Comparator<Category> comparator;
        if ("name".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER);
        } else if ("slug".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(Category::getSlug, String.CASE_INSENSITIVE_ORDER);
        } else {
            log.warn("unsupported sort: {}", sort);
            throw new CategoryException("Sort must be name or slug", HttpStatus.BAD_REQUEST);
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(direction)) {
            log.warn("unsupported direction: {}", direction);
            throw new CategoryException("Direction must be asc or desc", HttpStatus.BAD_REQUEST);
        }

        return comparator.thenComparing(Category::getId);
    }
}
