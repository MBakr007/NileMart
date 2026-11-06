package org.route.service;

import lombok.RequiredArgsConstructor;
import org.route.cart.ShoppingCart;
import org.route.exception.StoreException;
import org.route.model.Order;
import org.route.model.Product;
import org.route.notification.NotificationSender;
import org.route.shipping.Governorate;
import org.route.shipping.ShippingQuoteService;
import org.route.util.MoneyFormat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> notificationSenders;

    public Order placeOrder(ShoppingCart cart, Governorate governorate) {
        // ORD-02: an empty cart cannot be checked out.
        if (cart == null || cart.isEmpty()) {
            throw new StoreException("Cart is empty");
        }

        Map<String, Integer> lines = new LinkedHashMap<>(cart.getLines());

        // ORD-03: validate every line before touching stock, so a failure
        // can never leave the order half-applied (ORD-06).
        for (Map.Entry<String, Integer> line : lines.entrySet()) {
            Product product = productService.findBySku(line.getKey());
            if (product.getStockQuantity() < line.getValue()) {
                throw new StoreException("Insufficient stock for " + product.getName()
                        + ": only " + product.getStockQuantity() + " available");
            }
        }

        BigDecimal subtotal = cart.subtotal();
        BigDecimal vat = taxCalculator.calculateVat(subtotal);
        BigDecimal shippingFee = shippingQuoteService.quote(governorate, subtotal);
        BigDecimal total = subtotal.add(vat).add(shippingFee).setScale(2, RoundingMode.HALF_UP);

        // Every line was validated above, so this loop cannot fail midway.
        for (Map.Entry<String, Integer> line : lines.entrySet()) {
            productService.reduceStock(line.getKey(), line.getValue());
        }

        Order order = new Order(
                orderRegistry.nextOrderNumber(),
                cart.getCustomer(),
                governorate,
                lines,
                subtotal,
                vat,
                shippingFee,
                total);
        orderRegistry.register(order);

        String message = "Order " + order.orderNumber() + " confirmed. Total "
                + MoneyFormat.format(total) + " EGP";
        for (NotificationSender sender : notificationSenders) {
            sender.send(cart.getCustomer(), message);
        }

        cart.clear();
        return order;
    }
}
