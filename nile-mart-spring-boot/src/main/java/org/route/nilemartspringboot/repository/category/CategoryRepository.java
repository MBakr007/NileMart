package org.route.nilemartspringboot.repository.category;

import org.route.nilemartspringboot.exception.CategoryNotFoundException;
import org.route.nilemartspringboot.model.Category;

import java.io.FileNotFoundException;
import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();

    Category findById(String id) throws CategoryNotFoundException;

    void save(Category category);

    void delete(String id);
}
