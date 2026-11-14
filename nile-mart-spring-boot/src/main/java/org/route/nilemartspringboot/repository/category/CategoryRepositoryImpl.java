package org.route.nilemartspringboot.repository.category;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.route.nilemartspringboot.exception.CategoryNotFoundException;
import org.route.nilemartspringboot.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository{

    private final List<Category> categoryList;

    public CategoryRepositoryImpl(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @PostConstruct
    public void init(){
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {

            String name = faker.commerce().department();

            Category category = Category.builder()
                    .id(String.valueOf(i + 1))
                    .name(name)
                    .slug(name.toLowerCase().replace(" ", "-"))
                    .active(random.nextBoolean())
                    .build();

            categoryList.add(category);
        }
    }

    @Override
    public List<Category> findAll() {
        return categoryList;
    }

    @Override
    public Category findById(String id) {
        return categoryList.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    public void save(Category category) {
        categoryList.add(category);
    }

    @Override
    public void delete(String id) {
        categoryList.removeIf(c -> c.getId().equals(id));
    }
}
