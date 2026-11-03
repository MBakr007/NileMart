package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
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

        //checking if the cart is empty
        if (cart.isEmpty()) {
            throw new StoreException("The cart is empty.");
        }

        //checking again all the products stock
        for (Map.Entry<String, Integer> entry : cart.getLines().entrySet()) {

            Product product = productService.findBySku(entry.getKey());
            int qty = entry.getValue();

            if (product.getStockQuantity() < qty) {
                throw new StoreException(
                        "Not enough stock for product: " + entry.getKey()
                );
            }
        }

        //calculating the subtotal
        BigDecimal subtotal = cart.subtotal();

        //calculating the vat
        BigDecimal vat = taxCalculator.calculate(subtotal);

        //calculating the shipping
        BigDecimal shippingFee =
                shippingQuoteService.quote(governorate, subtotal);

        //calculating the final total
        BigDecimal total = subtotal
                .add(vat)
                .add(shippingFee);

        //reducing the stock after all products are checked
        for (Map.Entry<String, Integer> entry : cart.getLines().entrySet()) {
            productService.reduceStock(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        //generating the order number
        String orderNumber = orderRegistry.generateOrderNumber();

        //copying the cart lines before clearing the cart
        Map<String, Integer> orderLines =
                new HashMap<>(cart.getLines());

        //creating the order
        Order order = new Order(
                orderNumber,
                cart.getCustomer(),
                governorate,
                orderLines,
                subtotal,
                vat,
                shippingFee,
                total
        );

        //saving the order
        orderRegistry.save(order);

        //creating the confirmation message
        String message =
                "Order " + orderNumber +
                        " confirmed. Total " + total + " EGP";

        //sending the notification with all the senders
        for (NotificationSender sender : notificationSenders) {
            sender.sendNotification(cart.getCustomer(), message);
        }

        //clearing the cart
        cart.clear();

        return order;
    }
}