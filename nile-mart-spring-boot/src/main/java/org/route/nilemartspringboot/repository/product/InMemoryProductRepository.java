package org.route.nilemartspringboot.repository.product;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.route.nilemartspringboot.exception.ProductException;
import org.route.nilemartspringboot.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    List<Product> myProducts = new ArrayList<>();

    @PostConstruct
    void init() {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {

            Product product = new Product();

            product.setSku("SKU-" + (1000 + i));
            product.setName(faker.commerce().productName());
            product.setCategory(faker.commerce().department());

            product.setPrice(
                    BigDecimal.valueOf(
                            10 + random.nextDouble() * 990
                    ).setScale(2, java.math.RoundingMode.HALF_UP)
            );

            product.setInStock(random.nextInt(101));
            product.setActive(random.nextBoolean());

            myProducts.add(product);
        }
    }

    @Override
    public List<Product> findAll() {
        return myProducts;
    }

    @Override
    public Product findBySku(String sku) {
        for (Product product : myProducts) {
            if (product.getSku().equals(sku)) {
                return product;
            }
        }
        throw new ProductException("Product with id: " + sku + " not found",
                HttpStatus.NOT_FOUND);
    }

    @Override
    public void save(Product product) {
        myProducts.add(product);
    }
}
