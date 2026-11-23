package org.route.nilemartspringboot.repository.category;

import org.route.nilemartspringboot.model.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();

    void save(Category category);

    Category findById(String id);

    void deleteById(String id);

    void updateActiveById(String id, boolean active);
}
