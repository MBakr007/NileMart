package controller;

import modal.Product;
import service.ProductService;
import view.ProductViewer;

import java.util.List;

public class ProductController {

    private final ProductViewer viewer;
    private final ProductService productService;
    public ProductController(ProductViewer viewer,  ProductService productService) {
        this.viewer = viewer;
        this.productService = productService;
    }

    public void listAvailableProducts() {
        List<Product> availableProducts = productService.getAvailableProducts();
        viewer.printProducts(availableProducts);
    }
}
