package org.route;

import org.route.cart.ShoppingCart;
import org.route.config.AppConfig;
import org.route.exception.StoreException;
import org.route.model.Customer;
import org.route.model.Order;
import org.route.model.Product;
import org.route.service.CheckoutService;
import org.route.service.ProductService;
import org.route.shipping.Governorate;
import org.route.util.MoneyFormat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Interactive console entry point. All pricing, stock and checkout rules live
 * in the service layer; this class only handles input and display.
 */
public class Main {

    private final AnnotationConfigApplicationContext context;
    private final ProductService productService;
    private final CheckoutService checkoutService;
    private final Scanner scanner = new Scanner(System.in);
    private final Map<Integer, ShoppingCart> carts = new LinkedHashMap<>();
    private int nextCartNumber = 1;

    public Main(AnnotationConfigApplicationContext context) {
        this.context = context;
        this.productService = context.getBean(ProductService.class);
        this.checkoutService = context.getBean(CheckoutService.class);
    }

    public static void main(String[] args) {
        // try-with-resources closes the context, firing every @PreDestroy hook.
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            new Main(context).run();
        }
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> showProducts();
                    case 2 -> createCart();
                    case 3 -> addItemToCart();
                    case 4 -> showCart();
                    case 5 -> checkoutCart();
                    case 0 -> running = false;
                    default -> System.out.println("Unknown option: " + choice);
                }
            } catch (StoreException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========== NILEMART ==========");
        System.out.println("1. Show products  2. Create a cart  3. Add a product to a cart  "
                + "4. Show a cart  5. Check out a cart  0. Exit");
    }

    private void showProducts() {
        System.out.println("SKU           NAME                          PRICE      STOCK");
        for (Product product : productService.findAll()) {
            System.out.printf("%-13s %-29s %-10s %d%n",
                    product.getSku(),
                    product.getName(),
                    MoneyFormat.format(product.getPrice()),
                    product.getStockQuantity());
        }
    }

    private void createCart() {
        String fullName = readText("Customer full name: ");
        String phone = readText("Phone: ");
        String email = readText("Email (leave blank if none): ");

        Customer customer = new Customer(fullName, phone, email.isBlank() ? null : email);
        ShoppingCart cart = context.getBean(ShoppingCart.class);
        cart.setCartNumber(nextCartNumber++);
        cart.setCustomer(customer);
        carts.put(cart.getCartNumber(), cart);

        System.out.println("Cart " + cart.getCartNumber() + " created for " + customer.fullName() + ".");
    }

    private void addItemToCart() {
        ShoppingCart cart = requireCart(readInt("Cart number: "));
        String sku = readText("Product SKU: ");
        int quantity = readInt("Quantity: ");
        cart.addItem(sku, quantity);
        System.out.println("Added " + quantity + " x " + sku + " to cart " + cart.getCartNumber() + ".");
    }

    private void showCart() {
        ShoppingCart cart = requireCart(readInt("Cart number: "));
        System.out.println("Cart " + cart.getCartNumber() + " for " + cart.getCustomer().fullName());

        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("SKU           NAME                          QTY        LINE TOTAL");
        for (Map.Entry<String, Integer> line : cart.getLines().entrySet()) {
            Product product = productService.findBySku(line.getKey());
            BigDecimal lineTotal = productService.getPrice(line.getKey(), line.getValue());
            System.out.printf("%-13s %-29s %-10d %s%n",
                    product.getSku(), product.getName(), line.getValue(), MoneyFormat.format(lineTotal));
        }
        System.out.println("Subtotal: " + MoneyFormat.format(cart.subtotal()) + " EGP");
    }

    private void checkoutCart() {
        ShoppingCart cart = requireCart(readInt("Cart number: "));
        Governorate governorate = chooseGovernorate();
        Order order = checkoutService.placeOrder(cart, governorate);

        System.out.println("Order " + order.orderNumber() + " placed.");
        System.out.println("Subtotal:     " + MoneyFormat.format(order.subtotal()) + " EGP");
        System.out.println("VAT:          " + MoneyFormat.format(order.vat()) + " EGP");
        System.out.println("Shipping:     " + MoneyFormat.format(order.shippingFee()) + " EGP");
        System.out.println("Total:        " + MoneyFormat.format(order.total()) + " EGP");
    }

    private Governorate chooseGovernorate() {
        Governorate[] governorates = Governorate.values();
        System.out.println("Governorates:");
        for (int i = 0; i < governorates.length; i++) {
            System.out.println("  " + (i + 1) + ". " + governorates[i].getDisplayName()
                    + " (" + governorates[i].getZone() + ")");
        }
        while (true) {
            int selection = readInt("Governorate number: ");
            if (selection >= 1 && selection <= governorates.length) {
                return governorates[selection - 1];
            }
            System.out.println("Invalid governorate number. Choose 1-" + governorates.length + ".");
        }
    }

    private ShoppingCart requireCart(int cartNumber) {
        ShoppingCart cart = carts.get(cartNumber);
        if (cart == null) {
            throw new StoreException("Cart not found: " + cartNumber);
        }
        return cart;
    }

    private int readInt(String prompt) {
        while (true) {
            String value = readText(prompt);
            if (value.isEmpty() && !scanner.hasNextLine()) {
                return 0;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
    }
}
