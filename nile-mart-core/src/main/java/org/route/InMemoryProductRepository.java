package org.route;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository{

    private final HashMap<String, Product> store = new HashMap<>();

    @PostConstruct
    void saveProducts() {
        Product p1 = Product.builder()
                .name("Smartphone X5 128GB")
                .sku("PH-X5-128")
                .price(BigDecimal.valueOf(16000))
                .category("Electronics")
                .isActive(true)
                .stockQty(12)
                .build();
        Product p2 = Product.builder()
                .name("Fast Charger 20W")
                .sku("ACC-CHG-20W")
                .price(BigDecimal.valueOf(650))
                .category("Electronics")
                .isActive(true)
                .stockQty(40)
                .build();
        Product p3 = Product.builder()
                .sku("ACC-BUDS-6")
                .name("Wireless Earbuds")
                .category("Electronics")
                .price(BigDecimal.valueOf(1100))
                .stockQty(8)
                .isActive(true)
                .build();

        Product p4 = Product.builder()
                .sku("FSH-GAL-001")
                .name("Cotton Galabeya")
                .category("Fashion")
                .price(BigDecimal.valueOf(900))
                .stockQty(25)
                .isActive(true)
                .build();

        Product p5 = Product.builder()
                .sku("HOM-PAN-28")
                .name("Non-stick Frying Pan 28cm")
                .category("Home & Kitchen")
                .price(BigDecimal.valueOf(1200))
                .stockQty(3)
                .isActive(true)
                .build();

        Product p6 = Product.builder()
                .sku("HOM-SPC-RCK")
                .name("Wooden Spice Rack")
                .category("Home & Kitchen")
                .price(BigDecimal.valueOf(750))
                .stockQty(10)
                .isActive(true)
                .build();

        Product p7 = Product.builder()
                .sku("BTY-SUN-50")
                .name("Sun Cream SPF50")
                .category("Beauty")
                .price(BigDecimal.valueOf(350))
                .stockQty(0)
                .isActive(true)
                .build();

        Product p8 = Product.builder()
                .sku("BK-CAI-TRI")
                .name("The Cairo Trilogy")
                .category("Books")
                .price(BigDecimal.valueOf(450))
                .stockQty(15)
                .isActive(true)
                .build();


        store.put(p1.getSku(), p1);
        store.put(p2.getSku(), p2);
        store.put(p3.getSku(), p3);
        store.put(p4.getSku(), p4);
        store.put(p5.getSku(), p5);
        store.put(p6.getSku(), p6);
        store.put(p7.getSku(), p7);
        store.put(p8.getSku(), p8);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(store.get(sku));
    }

    @Override
    public void save(Product product) {
        store.put(product.getSku(), product);
    }

    @PreDestroy
    void printCatalogClosed(){
        System.out.println("Catalog closed");
    }


}
