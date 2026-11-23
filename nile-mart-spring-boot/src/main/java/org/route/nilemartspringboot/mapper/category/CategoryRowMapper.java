package org.route.nilemartspringboot.mapper.category;

import org.route.nilemartspringboot.model.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryRowMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setSlug(rs.getString("slug"));
        category.setActive(rs.getBoolean("active"));
        return category;
    }
}
