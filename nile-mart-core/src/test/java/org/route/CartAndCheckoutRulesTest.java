package org.route;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.route.cart.ShoppingCart;
import org.route.config.AppConfig;
import org.route.exception.StoreException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.Product;
import org.route.service.CheckoutService;
import org.route.service.ProductService;
import org.route.shipping.Governorate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartAndCheckoutRulesTest {

    private AnnotationConfigApplicationContext context;
    private ProductService productService;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        productService = context.getBean(ProductService.class);
        checkoutService = context.getBean(CheckoutService.class);
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void outOfStockProductIsRejected() {
        ShoppingCart cart = newCart(1);

        StoreException error = assertThrows(StoreException.class,
                () -> cart.addItem("BTY-SUN-50", 1));

        assertTrue(error.getMessage().contains("out of stock"));
        assertTrue(cart.isEmpty());
    }

    @Test
    void quantityAboveStockIsRejectedWithAvailableCount() {
        ShoppingCart cart = newCart(1);

        StoreException error = assertThrows(StoreException.class,
                () -> cart.addItem("HOM-PAN-28", 4));

        assertTrue(error.getMessage().toLowerCase().contains("only 3 available"));
        assertTrue(cart.isEmpty());
    }

    @Test
    void quantityAbovePerProductLimitIsRejected() {
        ShoppingCart cart = newCart(1);

        StoreException error = assertThrows(StoreException.class,
                () -> cart.addItem("ACC-CHG-20W", 11));

        assertTrue(error.getMessage().contains("Maximum 10"));
        assertTrue(cart.isEmpty());
    }

    @Test
    void repeatedSkuMergesIntoOneLine() {
        ShoppingCart cart = newCart(1);

        cart.addItem("ACC-CHG-20W", 1);
        cart.addItem("ACC-CHG-20W", 2);

        assertEquals(1, cart.getLines().size());
        assertEquals(3, cart.getLines().get("ACC-CHG-20W"));
    }

    @Test
    void cartsAreIndependentBecauseScopeIsPrototype() {
        ShoppingCart first = newCart(1);
        ShoppingCart second = newCart(2);

        assertNotSame(first, second);

        first.addItem("ACC-CHG-20W", 1);

        assertTrue(second.isEmpty());
        assertEquals(1, first.getLines().size());
    }

    @Test
    void failedSecondCheckoutLeavesStockUntouched() {
        ShoppingCart first = newCart(1);
        ShoppingCart second = newCart(2);
        first.addItem("ACC-BUDS-6", 2);
        second.addItem("ACC-BUDS-6", 7);

        Order secondOrder = checkoutService.placeOrder(second, Governorate.CAIRO);
        assertEquals(7, secondOrder.lines().get("ACC-BUDS-6"));

        StoreException error = assertThrows(StoreException.class,
                () -> checkoutService.placeOrder(first, Governorate.CAIRO));

        assertTrue(error.getMessage().contains("only 1 available"));
        Product earbuds = productService.findBySku("ACC-BUDS-6");
        assertEquals(1, earbuds.getStockQuantity());
        assertEquals(2, first.getLines().get("ACC-BUDS-6"));
    }

    @Test
    void emptyCartCannotBeCheckedOut() {
        ShoppingCart cart = newCart(1);

        StoreException error = assertThrows(StoreException.class,
                () -> checkoutService.placeOrder(cart, Governorate.GIZA));

        assertEquals("Cart is empty", error.getMessage());
    }

    private ShoppingCart newCart(int number) {
        ShoppingCart cart = context.getBean(ShoppingCart.class);
        cart.setCartNumber(number);
        cart.setCustomer(new Customer("Customer " + number, "0100000000" + number, null));
        return cart;
    }
}
