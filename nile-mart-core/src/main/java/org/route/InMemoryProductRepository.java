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


    //it works like a seeder
    @PostConstruct
    void saveProducts() {
        Product p1 = Product.builder()
                .name("Smartphone X5 128GB")
                .sku("PH-X5-128")
                .price(BigDecimal.valueOf(16000.00))
                .stockQuantity(12)
                .category("Electronics")
                .isActive(true)
                .build();
        Product p2 = Product.builder()
                .name("Fast Charger 20W")
                .sku("ACC-CHG-20W")
                .price(BigDecimal.valueOf(650.00))
                .stockQuantity(40)
                .category("Electronics")
                .isActive(true)
                .build();
        Product p3 = Product.builder()
                .name("Wireless Earbuds")
                .sku("ACC-BUDS-6")
                .price(BigDecimal.valueOf(1100.00))
                .stockQuantity(8)
                .category("Electronics")
                .isActive(true)
                .build();
        Product p4 = Product.builder()
                .name("Cotton Galabeya")
                .sku("FSH-GAL-001")
                .price(BigDecimal.valueOf(900.00))
                .stockQuantity(25)
                .category("Fashion")
                .isActive(true)
                .build();
        Product p5 = Product.builder()
                .name("Non-stick Frying Pan 28cm")
                .sku("HOM-PAN-28")
                .price(BigDecimal.valueOf(1200.00))
                .stockQuantity(3)
                .category("Home & Kitchen")
                .isActive(true)
                .build();
        Product p6 = Product.builder()
                .name("Wooden Spice Rack")
                .sku("HOM-SPC-RCK")
                .price(BigDecimal.valueOf(750.00))
                .stockQuantity(10)
                .category("Home & Kitchen")
                .isActive(true)
                .build();
        Product p7 = Product.builder()
                .name("Sun Cream SPF50")
                .sku("BTY-SUN-50")
                .price(BigDecimal.valueOf(350.00))
                .stockQuantity(0)
                .category("Beauty")
                .isActive(true)
                .build();
        Product p8 = Product.builder()
                .name("The Cairo Trilogy")
                .sku("BK-CAI-TRI")
                .price(BigDecimal.valueOf(450.00))
                .stockQuantity(15)
                .category("Books")
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
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(store.get(sku));
    }

    //here i can save all the values that are in the store hashmap, and then return all of them in the find all method.
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(Product product) {
        store.put(product.getSku(), product);
    }

    //pre destroy method
    @PreDestroy
    public void closeCatalog(){
        System.out.println("Catalog closed");
    }

}
