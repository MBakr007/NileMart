package org.route.cart;

import lombok.Getter;
import lombok.Setter;
import org.route.exception.StoreException;
import org.route.model.Customer;
import org.route.model.Product;
import org.route.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A single customer cart.
 *
 * <p>Prototype scope is intentional: every cart needs its own basket and its
 * own runtime customer/number, so the container must hand out a new instance
 * for every {@code getBean(ShoppingCart.class)} call.</p>
 */
@Component
@Scope("prototype")
public class ShoppingCart {

    private final ProductService productService;
    private final int maxQuantityPerProduct;

    @Getter
    @Setter
    private int cartNumber;

    @Getter
    @Setter
    private Customer customer;

    /** SKU to quantity; LinkedHashMap preserves the order items were added. */
    private final Map<String, Integer> lines = new LinkedHashMap<>();

    public ShoppingCart(ProductService productService,
                        @Value("${store.max-quantity-per-product}") int maxQuantityPerProduct) {
        this.productService = productService;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }

    /**
     * Adds a quantity of a SKU to the cart, merging with an existing line.
     * Fails before mutating the cart if any CART rule would be broken.
     */
    public void addItem(String sku, int qty) {
        if (qty <= 0) {
            throw new StoreException("Quantity must be greater than zero.");
        }

        Product product = productService.findBySku(sku);
        if (product.getStockQuantity() <= 0) {
            throw new StoreException(product.getName() + " is out of stock");
        }

        int currentQuantity = lines.getOrDefault(sku, 0);
        int desiredQuantity = currentQuantity + qty;
        int allowedQuantity = Math.min(maxQuantityPerProduct, product.getStockQuantity());

        if (desiredQuantity > allowedQuantity) {
            if (desiredQuantity > product.getStockQuantity()) {
                throw new StoreException("Only " + product.getStockQuantity()
                        + " available for " + product.getName());
            }
            throw new StoreException("Maximum " + maxQuantityPerProduct
                    + " units per product for " + product.getName());
        }

        lines.put(sku, desiredQuantity);
    }

    public BigDecimal subtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> line : lines.entrySet()) {
            subtotal = subtotal.add(productService.getPrice(line.getKey(), line.getValue()));
        }
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void clear() {
        lines.clear();
    }

    /** Read-only view used for display and for defensive checkout snapshots. */
    public Map<String, Integer> getLines() {
        return Collections.unmodifiableMap(lines);
    }
}
