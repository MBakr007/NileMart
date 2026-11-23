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
import org.route.nilemartspringboot.dto.product.AddProductRequest;
import org.route.nilemartspringboot.dto.product.ProductResponse;
import org.route.nilemartspringboot.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST endpoints for browsing and adding products.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Browse and add products")
@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get a page of products",
            description = "Filters by category, sorts, then returns the requested page. "
                    + "The content is empty if nothing matches or the page is past the last one.")
    @ApiResponse(responseCode = "200", description = "Page of products")
    @ApiResponse(responseCode = "400", description = "Invalid request parameter", content = @Content)
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @Parameter(description = "Number of products per page, 10 if not sent", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(10) int size,
            @Parameter(description = "Page number, starting from zero, 0 if not sent", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Field to sort by",
                    schema = @Schema(allowableValues = {"price", "name"}, defaultValue = "price"))
            @RequestParam(defaultValue = "price") String sort,
            @Parameter(description = "Sort direction",
                    schema = @Schema(allowableValues = {"asc", "desc"}, defaultValue = "asc"))
            @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Category to filter by (case-insensitive), all categories if not sent")
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(productService.getAllProducts(size, page, sort, direction, category));
    }

    @Operation(summary = "Get available products", description = "Returns every product that is in stock.")
    @ApiResponse(responseCode = "200", description = "Products in stock")
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    @Operation(summary = "Get products with VAT",
            description = "Returns every product with VAT added to its price. Stored prices are not changed.")
    @ApiResponse(responseCode = "200", description = "Products with VAT-inclusive prices")
    @GetMapping("/vat")
    public ResponseEntity<List<ProductResponse>> getProductVat() {
        return ResponseEntity.ok(productService.getAllProductWithVat());
    }

    @Operation(summary = "Get a product by id")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "400", description = "The id is not a number", content = @Content)
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "Product id", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Add a product", description = "Saves a new product and returns it with its generated id.")
    @ApiResponse(responseCode = "201", description = "Product created")
    @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody AddProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(request));
    }

}
