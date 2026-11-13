package org.route.nilemartspringboot.controller;

import jakarta.validation.Valid;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products =  productService.getAvailableProducts();
        model.addAttribute("availableProducts", products);
        return "products";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("product", new Product());
        return "productForm";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute("product") @Valid Product product,
                             BindingResult result) {
        if (productService.isProductExist(product.getSku())) {
            result.rejectValue("sku", "400", "sku is already exist");
        }
        if (result.hasErrors()) {
            return "productForm";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }


    @GetMapping("/product/vat")
    public String getProductVat(Model model) {
        List<Product> products =  productService.getAllProductWithVat();
        model.addAttribute("availableProducts", products);
        return "products";
    }

}
