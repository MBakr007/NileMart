package org.route;

import org.junit.jupiter.api.Test;
import org.route.model.Product;
import org.route.repository.InMemoryProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Locks the catalogue to the assignment data table so it cannot silently drift.
 */
class CatalogueSpecificationTest {

    @Test
    void repositoryLoadsTheEightSpecifiedProductsInOrder() {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        repository.seedProducts();

        List<Product> products = repository.findAll();

        assertEquals(8, products.size());
        assertProduct(products.get(0), "PH-X5-128", "Smartphone X5 128GB",
                "Electronics", "16000.00", 12);
        assertProduct(products.get(1), "ACC-CHG-20W", "Fast Charger 20W",
                "Electronics", "650.00", 40);
        assertProduct(products.get(2), "ACC-BUDS-6", "Wireless Earbuds",
                "Electronics", "1100.00", 8);
        assertProduct(products.get(3), "FSH-GAL-001", "Cotton Galabeya",
                "Fashion", "900.00", 25);
        assertProduct(products.get(4), "HOM-PAN-28", "Non-stick Frying Pan 28cm",
                "Home & Kitchen", "1200.00", 3);
        assertProduct(products.get(5), "HOM-SPC-RCK", "Wooden Spice Rack",
                "Home & Kitchen", "750.00", 10);
        assertProduct(products.get(6), "BTY-SUN-50", "Sun Cream SPF50",
                "Beauty", "350.00", 0);
        assertProduct(products.get(7), "BK-CAI-TRI", "The Cairo Trilogy",
                "Books", "450.00", 15);
    }

    private static void assertProduct(Product product, String sku, String name,
                                      String category, String price, int stock) {
        assertEquals(sku, product.getSku());
        assertEquals(name, product.getName());
        assertEquals(category, product.getCategory());
        assertEquals(new BigDecimal(price), product.getPrice());
        assertEquals(stock, product.getStockQuantity());
        assertTrue(product.isActive());
    }
}
