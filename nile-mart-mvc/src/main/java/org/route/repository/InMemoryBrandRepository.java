package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Brand;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryBrandRepository implements BrandRepository {
    private final List<Brand> brands = new ArrayList<>();

    //it works like a seeder
    @PostConstruct
    public void init() {
        brands.add(new Brand("Samsung", "samsung", true));
        brands.add(new Brand("Asus", "asus", true));
        brands.add(new Brand("Anker", "anker", true));
        brands.add(new Brand("Tefal", "tefal", true));
        brands.add(new Brand("Old Brand", "old-brand", false));
    }

    @Override
    public List<Brand> findAll() {
        return brands;
    }

    @Override
    public Brand findBySlug(String slug) {
        //finding the brand with the same slug
        for (Brand brand : brands) {
            if (brand.getSlug().equals(slug)) {
                return brand;
            }
        }
        return null;
    }

    @Override
    public void save(Brand brand) {
        //saving the brand in the list
        brands.add(brand);
    }
}
