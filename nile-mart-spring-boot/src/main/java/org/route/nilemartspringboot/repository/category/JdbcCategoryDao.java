package org.route.nilemartspringboot.repository.category;

import org.route.nilemartspringboot.exception.CategoryException;
import org.route.nilemartspringboot.model.Category;
import org.route.nilemartspringboot.repository.Database;
import org.springframework.http.HttpStatus;
//import org.route.nilemartspringboot.mapper.category.CategoryRowMapper;
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link CategoryDao} implementation that reads and writes categories using plain JDBC.
 */
@Repository
public class JdbcCategoryDao implements CategoryDao {
//    private final JdbcTemplate jdbcTemplate;
//    private final CategoryRowMapper categoryRowMapper;
//
//    public JdbcCategoryDao(JdbcTemplate jdbcTemplate, CategoryRowMapper categoryRowMapper) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.categoryRowMapper = categoryRowMapper;
//    }

    /**
     * Finds all categories, converting every row into a {@link Category}.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "select id, name, slug, active from categories";
     *
     * try {
     *     return jdbcTemplate.query(sql, categoryRowMapper);
     * } catch (DataAccessException e) {
     *     throw new CategoryException("Database error while fetching categories -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @return all categories, or an empty list if there are none
     * @throws CategoryException if a database error occurs
     */
    @Override
    public List<Category> findAll() {
        String sql = "select id, name, slug, active from categories";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapRow(rs));
            }
            return categories;

        } catch (SQLException e) {
            throw new CategoryException("Database error while fetching categories -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Saves a new category. The id is not inserted because the database generates it.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "insert into categories (name, slug, active) values (?, ?, ?)";
     *
     * try {
     *     int rows = jdbcTemplate.update(sql, category.getName(), category.getSlug(), category.getActive());
     *     if (rows == 0) {
     *         throw new CategoryException("Category was not saved", HttpStatus.INTERNAL_SERVER_ERROR);
     *     }
     * } catch (DataAccessException e) {
     *     throw new CategoryException("Database error while saving category -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param category the category to save
     * @throws CategoryException if no row was affected or a database error occurs
     */
    @Override
    public void save(Category category) {
        String sql = "insert into categories (name, slug, active) values (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getSlug());
            ps.setBoolean(3, category.getActive());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new CategoryException("Category was not saved", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException e) {
            throw new CategoryException("Database error while saving category -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Finds a category by its id. The {@link Connection}, {@link PreparedStatement} and
     * {@link ResultSet} are closed automatically by try-with-resources.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "select id, name, slug, active from categories where id = ?";
     *
     * try {
     *     return jdbcTemplate.queryForObject(sql, categoryRowMapper, id);
     * } catch (EmptyResultDataAccessException e) {
     *     throw new CategoryException("Category with id: " + id + " not found",
     *             HttpStatus.NOT_FOUND);
     * } catch (DataAccessException e) {
     *     throw new CategoryException("Database error while fetching category with id: " + id
     *             + " -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param id the category id
     * @return the matching category
     * @throws CategoryException with {@link HttpStatus#NOT_FOUND} if the category does not exist, or
     *                           {@link HttpStatus#INTERNAL_SERVER_ERROR} for any database problem
     */
    @Override
    public Category findById(String id) {
        String sql = "select id, name, slug, active from categories where id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            throw new CategoryException("Category with id: " + id + " not found",
                    HttpStatus.NOT_FOUND);

        } catch (SQLException e) {
            throw new CategoryException("Database error while fetching category with id: " + id
                    + " -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a category by its id.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "delete from categories where id = ?";
     *
     * try {
     *     int rows = jdbcTemplate.update(sql, id);
     *     if (rows == 0) {
     *         throw new CategoryException("Category with id: " + id + " not found",
     *                 HttpStatus.NOT_FOUND);
     *     }
     * } catch (DataAccessException e) {
     *     throw new CategoryException("Database error while deleting category with id: " + id
     *             + " -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param id the category id
     * @throws CategoryException with {@link HttpStatus#NOT_FOUND} if the category does not exist, or
     *                           {@link HttpStatus#INTERNAL_SERVER_ERROR} for any database problem
     */
    @Override
    public void deleteById(String id) {
        String sql = "delete from categories where id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new CategoryException("Category with id: " + id + " not found",
                        HttpStatus.NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new CategoryException("Database error while deleting category with id: " + id
                    + " -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Activates or deactivates a category by its id.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "update categories set active = ? where id = ?";
     *
     * try {
     *     int rows = jdbcTemplate.update(sql, active, id);
     *     if (rows == 0) {
     *         throw new CategoryException("Category with id: " + id + " not found",
     *                 HttpStatus.NOT_FOUND);
     *     }
     * } catch (DataAccessException e) {
     *     throw new CategoryException("Database error while updating active of category with id: " + id
     *             + " -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param id     the category id
     * @param active the new active value
     * @throws CategoryException with {@link HttpStatus#NOT_FOUND} if the category does not exist, or
     *                           {@link HttpStatus#INTERNAL_SERVER_ERROR} for any database problem
     */
    @Override
    public void updateActiveById(String id, boolean active) {
        String sql = "update categories set active = ? where id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setString(2, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new CategoryException("Category with id: " + id + " not found",
                        HttpStatus.NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new CategoryException("Database error while updating active of category with id: " + id
                    + " -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Converts the current row of the {@link ResultSet} into a {@link Category}
     * (same job as {@code CategoryRowMapper} does for {@code JdbcTemplate}).
     *
     * @param rs the result set, positioned on the row to convert
     * @return the converted category
     * @throws SQLException if a column cannot be read
     */
    private Category mapRow(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setSlug(rs.getString("slug"));
        category.setActive(rs.getBoolean("active"));
        return category;
    }
}
