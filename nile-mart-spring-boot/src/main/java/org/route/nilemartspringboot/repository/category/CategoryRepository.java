package org.route.nilemartspringboot.repository.category;

import org.route.nilemartspringboot.model.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();

    Category findById(String id);

    void save(Category category);

    void delete(String id);
}
