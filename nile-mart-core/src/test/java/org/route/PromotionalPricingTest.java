package org.route;

import org.junit.jupiter.api.AfterEach;
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

class PromotionalPricingTest {

    private AnnotationConfigApplicationContext context;

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void promotionalPricing_appliesTwentyPercentDiscount() {
        context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.addBeanFactoryPostProcessor(beanFactory -> {
            beanFactory.getBeanDefinition("standardPricingService").setPrimary(false);
            beanFactory.getBeanDefinition("promotionalPricingService").setPrimary(true);
        });
        context.refresh();

        ShoppingCart cart = context.getBean(ShoppingCart.class);
        cart.setCartNumber(1);
        cart.setCustomer(new Customer("Mona", "01000000001", "mona@example.com"));
        cart.addItem("AC-CH20", 2);
        cart.addItem("CL-GAL-COT", 1);

        Order order = context.getBean(CheckoutService.class)
                .placeOrder(cart, Governorate.GIZA);

        assertMoney("1760.00", order.subtotal());
        assertMoney("246.40", order.vat());
        assertMoney("50.00", order.shippingFee());
        assertMoney("2056.40", order.total());
    }

    private static void assertMoney(String expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }
}
