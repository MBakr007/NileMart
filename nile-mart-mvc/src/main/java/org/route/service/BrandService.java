package org.route.service;

import org.route.model.Brand;
import org.route.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Brand> getActiveBrands() {
        //getting only the active brands
        List<Brand> activeBrands = new ArrayList<>();
        for (Brand brand : brandRepository.findAll()) {
            if (brand.isActive()) {
                activeBrands.add(brand);
            }
        }
        return activeBrands;
    }

    public boolean isActiveBrand(String slug) {
        //checking if the brand exists and is active
        Brand brand = brandRepository.findBySlug(slug);
        return brand != null && brand.isActive();
    }

    public String getBrandName(String slug) {
        //showing a dash when the product has no brand
        if (slug == null || slug.isEmpty()) {
            return "—";
        }
        Brand brand = brandRepository.findBySlug(slug);
        return brand == null ? slug : brand.getName();
    }

    public boolean saveBrand(Brand brand, Errors errors) {
        //checking if the brand name is already used
        for (Brand existingBrand : brandRepository.findAll()) {
            if (existingBrand.getName().equalsIgnoreCase(brand.getName())) {
                errors.rejectValue("name", "brand.name.duplicate", "Brand name already exists");
                break;
            }
        }

        //checking if the slug is already used
        if (brandRepository.findBySlug(brand.getSlug()) != null) {
            errors.rejectValue("slug", "brand.slug.duplicate", "Slug already exists");
        }

        //returning without saving when there are errors
        if (errors.hasErrors()) {
            return false;
        }

        //saving the brand
        brandRepository.save(brand);
        return true;
    }
}
