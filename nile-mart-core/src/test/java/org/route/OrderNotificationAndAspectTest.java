package org.route;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.route.cart.ShoppingCart;
import org.route.config.AppConfig;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.service.CheckoutService;
import org.route.service.OrderRegistry;
import org.route.shipping.Governorate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderNotificationAndAspectTest {

    private AnnotationConfigApplicationContext context;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        checkoutService = context.getBean(CheckoutService.class);
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void orderNumbersAndRevenueFollowTheSpecification() {
        Order first = checkoutService.placeOrder(cart(1, "Mona", "01000000001", "mona@example.com",
                "AC-CH20", 2, "CL-GAL-COT", 1), Governorate.GIZA);
        Order second = checkoutService.placeOrder(cart(2, "Omar", "01000000002", null,
                "PH-X5-128", 1), Governorate.ASWAN);


        assertTrue(first.orderNumber().matches("NM-\\d{8}-00001"));
        assertTrue(second.orderNumber().matches("NM-\\d{8}-00002"));

        OrderRegistry registry = context.getBean(OrderRegistry.class);
        assertEquals(2, registry.orderCount());
        assertMoney("20798.00", registry.totalRevenue());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(output));
            context.close();
            context = null;
        } finally {
            System.setOut(original);
        }
        assertTrue(output.toString().contains("End of day: 2 orders, revenue 20,798.00 EGP"));
        assertTrue(output.toString().contains("Catalog closed"));
    }

    @Test
    void notificationFormatAndEmailFallbackAreCorrect() {
        ShoppingCart cart = cart(1, "Mona", "01000000001", null,
                "AC-CH20", 2, "CL-GAL-COT", 1);

        ByteArrayOutputStream output = capture(() ->
                checkoutService.placeOrder(cart, Governorate.GIZA));
        String log = output.toString();

        assertTrue(log.contains("[SMS] to 01000000001: Order NM-"));
        assertTrue(log.contains("confirmed. Total 2,558.00 EGP"));
        assertFalse(log.contains("[EMAIL]"));
    }

    @Test
    void emailSenderRunsWhenEmailExists() {
        ShoppingCart cart = cart(1, "Mona", "01000000001", "mona@example.com",
                "AC-CH20", 2, "CL-GAL-COT", 1);

        String log = capture(() -> checkoutService.placeOrder(cart, Governorate.GIZA)).toString();

        assertTrue(log.contains("[EMAIL] to mona@example.com: Order NM-"));
    }

    @Test
    void aspectsLogAuditAndExecutionTiming() {
        ShoppingCart cart = cart(7, "Mona", "01000000001", "mona@example.com",
                "AC-CH20", 2, "CL-GAL-COT", 1);

        String log = capture(() -> checkoutService.placeOrder(cart, Governorate.GIZA)).toString();

        assertTrue(log.contains("[AuditLog] Checkout started for cart 7 to Giza"));
        assertTrue(log.contains("[ExecutionLog] CheckoutService.placeOrder(..)"));
        assertTrue(log.contains(" ms"));
    }

    @Test
    void orderLinesAreDefensivelyCopiedAndImmutable() {
        Map<String, Integer> source = new LinkedHashMap<>();
        source.put("AC-CH20", 2);

        Order order = new Order("NM-20261107-00001",
                new Customer("Mona", "01000000001", null),
                Governorate.GIZA,
                source,
                new BigDecimal("1600.00"),
                new BigDecimal("224.00"),
                new BigDecimal("50.00"),
                new BigDecimal("1874.00"));

        source.put("CL-GAL-COT", 1);

        assertEquals(Map.of("AC-CH20", 2), order.lines());
        assertThrows(UnsupportedOperationException.class,
                () -> order.lines().put("PH-X5-128", 1));
    }

    private ShoppingCart cart(int number, String name, String phone, String email,
                              String sku, int qty) {
        ShoppingCart cart = context.getBean(ShoppingCart.class);
        cart.setCartNumber(number);
        cart.setCustomer(new Customer(name, phone, email));
        cart.addItem(sku, qty);
        return cart;
    }

    private ShoppingCart cart(int number, String name, String phone, String email,
                              String firstSku, int firstQty, String secondSku, int secondQty) {
        ShoppingCart cart = cart(number, name, phone, email, firstSku, firstQty);
        cart.addItem(secondSku, secondQty);
        return cart;
    }

    private static ByteArrayOutputStream capture(Runnable action) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(output));
            action.run();
        } finally {
            System.setOut(original);
        }
        return output;
    }

    private static void assertMoney(String expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }
}
