package org.route.nilemartspringboot.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.route.nilemartspringboot.dto.PageResponse;
import org.route.nilemartspringboot.dto.product.AddProductRequest;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        return ResponseEntity.ok(productService.getAvailableProducts());
    }


    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@Valid @RequestBody AddProductRequest request) {
        productService.saveProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/products/vat")
    public ResponseEntity<List<Product>> getProductVat() {
        return ResponseEntity.ok(productService.getAllProductWithVat());
    }


    @GetMapping("/products")
    public ResponseEntity<PageResponse<Product>> getAllProducts(@RequestParam(defaultValue = "10", required = false)
                                                                @Min(1) @Max(10) int size,
                                                                @RequestParam(defaultValue = "0", required = false)
                                                                @Min(0) int page) {
        return ResponseEntity.ok(productService.getAllProducts(size, page));
    }

}
