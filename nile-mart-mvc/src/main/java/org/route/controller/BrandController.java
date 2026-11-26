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
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "brands";
    }

    @GetMapping("/brands/new")
    public String showBrandForm(Model model) {
        model.addAttribute("brand", Brand.builder().build());
        return "brandForm";
    }

    @PostMapping("/brands")
    public String saveBrand(@ModelAttribute("brand") @Valid Brand brand,
                            BindingResult result) {
        FormErrors.rejectAll(result,
                brandService.validate(brand, FormErrors.fieldsWithErrors(result)));
        if (result.hasErrors()) {
            return "brandForm";
        }
        brandService.save(brand);
        return "redirect:/brands";
    }
}
