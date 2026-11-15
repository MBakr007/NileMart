package org.route.controller;

import jakarta.validation.Valid;
import org.route.model.Brand;
import org.route.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public String getBrands(Model model) {
        //getting the brands for the table
        model.addAttribute("brands", brandService.getAllBrands());
        return "brands";
    }

    @GetMapping("/brands/new")
    public String showForm(Model model) {
        //creating an empty brand for the form
        model.addAttribute("brand", new Brand());
        return "brandForm";
    }

    @PostMapping("/brands")
    public String addBrand(@ModelAttribute("brand") @Valid Brand brand, BindingResult result) {
        //saving the brand through the service
        if (!brandService.saveBrand(brand, result)) {
            //showing the same form with the errors
            return "brandForm";
        }
        //redirecting after saving the brand
        return "redirect:/brands";
    }
}
