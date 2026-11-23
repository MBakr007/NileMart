package org.route.nilemartspringboot.repository.product;

import org.route.nilemartspringboot.exception.ProductException;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.repository.Database;
import org.springframework.http.HttpStatus;
//import org.route.nilemartspringboot.mapper.ProductRowMapper;
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link JdbcProductDao} implementation that reads and writes products using plain JDBC.
 */
@Repository
public class JdbcProductDao implements ProductDao {
//    private final JdbcTemplate jdbcTemplate;
//    private final ProductRowMapper productRowMapper;
//
//    public InMemoryProductRepository(JdbcTemplate jdbcTemplate, ProductRowMapper productRowMapper) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.productRowMapper = productRowMapper;
//    }

    /**
     * Finds all products, converting every row into a {@link Product}.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "select id, name, category, price, in_stock, active from products";
     *
     * try {
     *     return jdbcTemplate.query(sql, productRowMapper);
     * } catch (DataAccessException e) {
     *     throw new ProductException("Database error while fetching products -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @return all products, or an empty list if there are none
     * @throws ProductException if a database error occurs
     */
    @Override
    public List<Product> findAll() {
        String sql = "select id, name, category, price, in_stock, active from products";
        List<Product> products = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }
            return products;

        } catch (SQLException e) {
            throw new ProductException("Database error while fetching products -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Saves a new product. The id is not inserted because the database generates it.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "insert into products (name, category, price, in_stock, active) values (?, ?, ?, ?, ?)";
     *
     * try {
     *     int rows = jdbcTemplate.update(sql, product.getName(), product.getCategory(),
     *             product.getPrice(), product.getInStock(), product.getActive());
     *     if (rows == 0) {
     *         throw new ProductException("Product was not saved", HttpStatus.INTERNAL_SERVER_ERROR);
     *     }
     * } catch (DataAccessException e) {
     *     throw new ProductException("Database error while saving product -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param product the product to save
     * @throws ProductException if no row was affected or a database error occurs
     */
    @Override
    public void save(Product product) {
        String sql = "insert into products (name, category, price, in_stock, active) values (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getInStock());
            ps.setString(5, product.getActive());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new ProductException("Product was not saved", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException e) {
            throw new ProductException("Database error while saving product -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Finds a product by its id. The {@link Connection}, {@link PreparedStatement} and
     * {@link ResultSet} are closed automatically by try-with-resources.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "select id, name, category, price, in_stock, active from products where id = ?";
     *
     * try {
     *     return jdbcTemplate.queryForObject(sql, productRowMapper, id);
     * } catch (EmptyResultDataAccessException e) {
     *     throw new ProductException("Product with id: " + id + " not found",
     *             HttpStatus.NOT_FOUND);
     * } catch (DataAccessException e) {
     *     throw new ProductException("Database error while fetching product with id: " + id
     *             + " -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param id the product id
     * @return the matching product
     * @throws ProductException with {@link HttpStatus#NOT_FOUND} if the product does not exist, or
     *                          {@link HttpStatus#INTERNAL_SERVER_ERROR} for any database problem
     *                          (connection failed, bad SQL, wrong column name, ...)
     */
    @Override
    public Product findById(String id) {

        String sql = "select id, name, category, price, in_stock, active from products where id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            throw new ProductException("Product with id: " + id + " not found",
                    HttpStatus.NOT_FOUND);

        } catch (SQLException e) {
            throw new ProductException("Database error while fetching product with id: " + id
                    + " -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a product by its id.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "delete from products where id = ?";
     *
     * try {
     *     int rows = jdbcTemplate.update(sql, id);
     *     if (rows == 0) {
     *         throw new ProductException("Product with id: " + id + " not found",
     *                 HttpStatus.NOT_FOUND);
     *     }
     * } catch (DataAccessException e) {
     *     throw new ProductException("Database error while deleting product with id: " + id
     *             + " -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param id the product id
     * @throws ProductException with {@link HttpStatus#NOT_FOUND} if the product does not exist, or
     *                          {@link HttpStatus#INTERNAL_SERVER_ERROR} for any database problem
     */
    @Override
    public void deleteById(String id) {
        String sql = "delete from products where id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new ProductException("Product with id: " + id + " not found",
                        HttpStatus.NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new ProductException("Database error while deleting product with id: " + id
                    + " -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the price of a product by its id.
     *
     * <p>Spring JdbcTemplate version (kept for reference):
     * <pre>{@code
     * String sql = "update products set price = ? where id = ?";
     *
     * try {
     *     int rows = jdbcTemplate.update(sql, price, id);
     *     if (rows == 0) {
     *         throw new ProductException("Product with id: " + id + " not found",
     *                 HttpStatus.NOT_FOUND);
     *     }
     * } catch (DataAccessException e) {
     *     throw new ProductException("Database error while updating price of product with id: " + id
     *             + " -> " + e.getMessage(),
     *             HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }</pre>
     *
     * @param id    the product id
     * @param price the new price
     * @throws ProductException with {@link HttpStatus#NOT_FOUND} if the product does not exist, or
     *                          {@link HttpStatus#INTERNAL_SERVER_ERROR} for any database problem
     */
    @Override
    public void updatePriceById(String id, BigDecimal price) {
        String sql = "update products set price = ? where id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, price);
            ps.setString(2, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new ProductException("Product with id: " + id + " not found",
                        HttpStatus.NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new ProductException("Database error while updating price of product with id: " + id
                    + " -> " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Converts the current row of the {@link ResultSet} into a {@link Product}
     * (same job as {@code ProductRowMapper} does for {@code JdbcTemplate}).
     *
     * @param rs the result set, positioned on the row to convert
     * @return the converted product
     * @throws SQLException if a column cannot be read
     */
    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setCategory(rs.getString("category"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setInStock(rs.getInt("in_stock"));
        product.setActive(rs.getString("active"));
        return product;
    }
}
