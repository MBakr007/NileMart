package org.route.nilemartspringboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.route.nilemartspringboot.dto.ErrorResponse;
import org.route.nilemartspringboot.dto.PageResponse;
import org.route.nilemartspringboot.dto.category.AddCategoryRequest;
import org.route.nilemartspringboot.dto.category.CategoryResponse;
import org.route.nilemartspringboot.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for browsing, adding and deleting categories.
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Browse, add and delete categories")
@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get a page of categories",
            description = "Filters by active, sorts, then returns the requested page. "
                    + "The content is empty if nothing matches or the page is past the last one.")
    @ApiResponse(responseCode = "200", description = "Page of categories")
    @ApiResponse(responseCode = "400", description = "Invalid request parameter", content = @Content)
    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(
            @Parameter(description = "Number of categories per page, 10 if not sent", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(10) int size,
            @Parameter(description = "Page number, starting from zero, 0 if not sent", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Field to sort by",
                    schema = @Schema(allowableValues = {"name", "slug"}, defaultValue = "name"))
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction",
                    schema = @Schema(allowableValues = {"asc", "desc"}, defaultValue = "asc"))
            @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Active value to filter by, all categories if not sent")
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(categoryService.getAllCategories(size, page, sort, direction, active));
    }

    @Operation(summary = "Get a category by id")
    @ApiResponse(responseCode = "200", description = "Category found")
    @ApiResponse(responseCode = "400", description = "The id is not a number", content = @Content)
    @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "Category id", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Add a category", description = "Saves a new category and returns it with its generated id.")
    @ApiResponse(responseCode = "201", description = "Category created")
    @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    @PostMapping
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody AddCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(request));
    }

    @Operation(summary = "Delete a category by id")
    @ApiResponse(responseCode = "204", description = "Category deleted")
    @ApiResponse(responseCode = "400", description = "The id is not a number", content = @Content)
    @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(
            @Parameter(description = "Category id", example = "1")
            @PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
