package org.route;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.route.cart.ShoppingCart;
import org.route.config.AppConfig;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.service.CheckoutService;
import org.route.shipping.Governorate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkedExamplesTest {

    private AnnotationConfigApplicationContext context;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        checkoutService = context.getBean(CheckoutService.class);
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void standardPricing_twoChargersAndGalabeyaToGiza() {
        ShoppingCart cart = newCart("Mona", "01000000001", "mona@example.com");
        cart.addItem("AC-CH20", 2);
        cart.addItem("CL-GAL-COT", 1);

        Order order = checkoutService.placeOrder(cart, Governorate.GIZA);

        assertMoney("2200.00", order.subtotal());
        assertMoney("308.00", order.vat());
        assertMoney("50.00", order.shippingFee());
        assertMoney("2558.00", order.total());
    }

    @Test
    void smartphoneToAswanShipsFree() {
        ShoppingCart cart = newCart("Omar", "01000000002", null);
        cart.addItem("PH-X5-128", 1);

        Order order = checkoutService.placeOrder(cart, Governorate.ASWAN);

        assertMoney("16000.00", order.subtotal());
        assertMoney("2240.00", order.vat());
        assertMoney("0.00", order.shippingFee());
        assertMoney("18240.00", order.total());
    }

    @Test
    void smartphoneToSouthSinaiIsNeverFree() {
        ShoppingCart cart = newCart("Salma", "01000000003", "salma@example.com");
        cart.addItem("PH-X5-128", 1);

        Order order = checkoutService.placeOrder(cart, Governorate.SOUTH_SINAI);

        assertMoney("16000.00", order.subtotal());
        assertMoney("2240.00", order.vat());
        assertMoney("120.00", order.shippingFee());
        assertMoney("18360.00", order.total());
    }

    private ShoppingCart newCart(String name, String phone, String email) {
        ShoppingCart cart = context.getBean(ShoppingCart.class);
        cart.setCartNumber(1);
        cart.setCustomer(new Customer(name, phone, email));
        return cart;
    }

    private static void assertMoney(String expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }
}
