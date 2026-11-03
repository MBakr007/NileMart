package org.route;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@Scope("prototype")
public class ShoppingCart {

    private final ProductService productService;
    private final int maxQuantityPerProduct;

    private int cartNumber;
    private Customer customer;

    //stores the sku and the quantity of the products
    private final Map<String, Integer> lines = new HashMap<>();

    public ShoppingCart(
            ProductService productService,
            @Value("${store.max-quantity-per-product}") int maxQuantityPerProduct
    ) {
        this.productService = productService;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }


    public void addItem(String sku, int qty) {

        //getting the product
        Product product = productService.findBySku(sku);

        //checking if the product is out of stock
        if (product.getStockQuantity() == 0) {
            throw new StoreException("Product out of stock: " + sku);
        }

        //getting the current quantity, if there is no product it return 0
        int currentQty = lines.getOrDefault(sku, 0);

        //adding the new quantity to the old one
        int newQty = currentQty + qty;

        //checking the max quantity allowed
        if (newQty > maxQuantityPerProduct) {
            throw new StoreException(
                    "Maximum quantity per product is " + maxQuantityPerProduct
            );
        }

        //checking if the stock is enough
        if (newQty > product.getStockQuantity()) {
            throw new StoreException(
                    "Not enough stock for product: " + sku
            );
        }

        //saving the product sku and the new quantity
        lines.put(sku, newQty);
    }

    public BigDecimal subtotal() {

        BigDecimal subtotal = BigDecimal.ZERO;

        //looping all the products in the cart
        for (Map.Entry<String, Integer> entry : lines.entrySet()) {

            String sku = entry.getKey();
            int qty = entry.getValue();

            //getting the price and adding it to subtotal
            subtotal = subtotal.add(
                    productService.getPrice(sku, qty)
            );
        }

        return subtotal;
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void clear() {
        lines.clear();
    }
}