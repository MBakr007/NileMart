package org.route.nilemartspringboot.service;

import lombok.extern.slf4j.Slf4j;
import org.route.nilemartspringboot.properties.StoreProperties;
import org.route.nilemartspringboot.dto.PageResponse;
import org.route.nilemartspringboot.dto.product.AddProductRequest;
import org.route.nilemartspringboot.dto.product.ProductResponse;
import org.route.nilemartspringboot.exception.ProductException;
import org.route.nilemartspringboot.mapper.product.ProductMapper;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.repository.product.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreProperties storeProperties;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, StoreProperties storeProperties, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.storeProperties = storeProperties;
        this.productMapper = productMapper;
    }

    /**
     * Finds all products that are in stock.
     *
     * @return the products with a stock quantity above zero
     */
    public List<ProductResponse> getAvailableProducts() {
        log.info("start getting available products");
        List<ProductResponse> availableProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.getInStock() > 0)
                availableProducts.add(productMapper.toProductResponse(p));
        }
        log.info("found {} available products", availableProducts.size());
        return availableProducts;
    }

    /**
     * Finds one page of products. The products are filtered by category, then sorted, then cut
     * down to the requested page.
     *
     * @param size      the number of products per page, at least 1
     * @param page      the page number, starting from zero
     * @param sort      the field to sort by: {@code price} or {@code name}
     * @param direction the sort direction: {@code asc} or {@code desc}
     * @param category  the category to filter by (case-insensitive), or {@code null} or blank for all categories
     * @return the requested page, with empty content if it is past the last page
     * @throws ProductException with {@link HttpStatus#BAD_REQUEST} if the sort field or direction is not supported
     */
    public PageResponse<ProductResponse> getAllProducts(int size, int page, String sort, String direction, String category) {
        log.info("start getting products with size: {}, page: {}, sort: {}, direction: {}, category: {}",
                size, page, sort, direction, category);

        Comparator<Product> comparator = getComparator(sort, direction);

        List<Product> products = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (matchesCategory(p, category))
                products.add(p);
        }
        products.sort(comparator);

        int totalElements = products.size();
        int totalPages = totalElements / size;
        if (totalElements % size != 0) {
            totalPages++;
        }

        // long avoids an int overflow when the page number is very large
        long start = Math.min((long) page * size, totalElements);
        long end = Math.min(start + size, totalElements);

        List<ProductResponse> content = new ArrayList<>();
        for (int i = (int) start; i < end; i++) {
            content.add(productMapper.toProductResponse(products.get(i)));
        }

        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setContent(content);
        pageResponse.setTotalElements(totalElements);
        pageResponse.setTotalPages(totalPages);

        log.info("returning {} of {} products", content.size(), totalElements);
        return pageResponse;
    }

    /**
     * Saves a new product.
     *
     * @param request the product to save
     * @return the saved product, including its generated id
     */
    public ProductResponse saveProduct(AddProductRequest request) {
        log.info("start saving product with name: {}", request.getName());
        Product savedProduct = productRepository.save(productMapper.toProduct(request));
        log.info("saved product with id: {}", savedProduct.getId());
        return productMapper.toProductResponse(savedProduct);
    }

    /**
     * Checks whether a product exists.
     *
     * @param id the product id
     * @return {@code true} if the product exists
     */
    public boolean isProductExist(Integer id) {
        boolean exists = productRepository.existsById(id);
        log.debug("product with id: {} exists: {}", id, exists);
        return exists;
    }

    /**
     * Finds all products with VAT added to their prices. The stored prices are not changed.
     *
     * @return all products with VAT-inclusive prices
     */
    public List<ProductResponse> getAllProductWithVat() {
        log.info("start getting products with vat rate: {}", storeProperties.getVatRate());
        List<ProductResponse> products = new ArrayList<>();
        BigDecimal totalVat = BigDecimal.valueOf(1).add(storeProperties.getVatRate());
        for (Product p : productRepository.findAll()) {
            ProductResponse response = productMapper.toProductResponse(p);
            response.setPrice(p.getPrice().multiply(totalVat));
            products.add(response);
        }
        log.info("found {} products with vat", products.size());
        return products;
    }

    /**
     * Finds a product by its id.
     *
     * @param id the product id
     * @return the matching product
     * @throws ProductException with {@link HttpStatus#NOT_FOUND} if the product does not exist
     */
    public ProductResponse getProductById(Integer id) {
        log.info("start getting product with id: {}", id);
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> {
                    log.warn("product with id: {} not found", id);
                    return new ProductException("Product with id: " + id + " not found",
                            HttpStatus.NOT_FOUND);
                });
    }

    /**
     * Checks whether a product belongs to a category.
     *
     * @param product  the product to check
     * @param category the category, or {@code null} or blank to match every product
     * @return {@code true} if the product matches
     */
    private boolean matchesCategory(Product product, String category) {
        return category == null || category.isBlank()
                || category.trim().equalsIgnoreCase(product.getCategory());
    }

    /**
     * Builds the comparator used to sort products. Products with equal values are ordered by id,
     * so a product never moves between pages.
     *
     * @param sort      the field to sort by: {@code price} or {@code name}
     * @param direction the sort direction: {@code asc} or {@code desc}
     * @return the comparator
     * @throws ProductException with {@link HttpStatus#BAD_REQUEST} if the sort field or direction is not supported
     */
    private Comparator<Product> getComparator(String sort, String direction) {
        Comparator<Product> comparator;
        if ("price".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if ("name".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
        } else {
            log.warn("unsupported sort: {}", sort);
            throw new ProductException("Sort must be price or name", HttpStatus.BAD_REQUEST);
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(direction)) {
            log.warn("unsupported direction: {}", direction);
            throw new ProductException("Direction must be asc or desc", HttpStatus.BAD_REQUEST);
        }

        return comparator.thenComparing(Product::getId);
    }
}
