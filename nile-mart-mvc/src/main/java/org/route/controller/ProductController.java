package org.route.controller;

import jakarta.validation.Valid;
import org.route.model.Product;
import org.route.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("availableProducts", productService.getAvailableProducts());
        return "products";
    }

    @GetMapping({"/products/new", "/new"})
    public String showForm(Model model) {
        model.addAttribute("product", new Product());
        addFormOptions(model);
        return "productForm";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute("product") @Valid Product product,
                             BindingResult result, Model model) {
        FormErrors.rejectAll(result,
                productService.validate(product, FormErrors.fieldsWithErrors(result)));
        if (result.hasErrors()) {
            addFormOptions(model);
            return "productForm";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    private void addFormOptions(Model model) {
        model.addAttribute("subCategories", productService.getSubCategories());
        model.addAttribute("activeBrands", productService.getActiveBrands());
    }
}
