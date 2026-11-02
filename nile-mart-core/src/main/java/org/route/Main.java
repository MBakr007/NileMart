package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static InputHelper inputHelper=new InputHelper();
    static Map<Integer, ShoppingCart> carts = new HashMap<>();

    void main(){

        //        var context = new ClassPathXmlApplicationContext("beans.xml");


//        StandardPricingService standard = context.getBean(StandardPricingService.class);
//        StandardPricingService standard2 = context.getBean(StandardPricingService.class);

//        ProductService p = context.getBean(ProductService.class);
//
//        System.out.println(p);

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            int nextCartId = 1;
            int choice;

            do {
                showMenu();
                choice = inputHelper.readInt("Enter ur choice: ");

                try {
                    switch (choice) {
                        case 1 -> {
                            ProductService productService=context.getBean(ProductService.class);
                            ProductViewer productViewer=context.getBean(ProductViewer.class);
                            productViewer.printProducts(productService.findAll());
                        }
                        case 2 -> {
                            String name = inputHelper.readString("Enter customer name: ");
                            String phone = inputHelper.readString("Enter phone: ");
                            String email = inputHelper.readString("Enter email (optional): ");
                            Customer customer=new Customer(name,phone,email.isBlank()?null:email);
                            ShoppingCart cart=context.getBean(ShoppingCart.class);
                            cart.setCartNumber(nextCartId++);
                            cart.setCustomer(customer);
                            carts.put(cart.getCartNumber(),cart);
                            System.out.println("Cart added successfully with ID "+cart.getCartNumber());
                        }
                        case 3 -> addProductToCart();
                        case 4 -> {
                            int cartId = inputHelper.readInt("Enter cart ID: ");
                            ShoppingCart cart=findCart(cartId);
                            viewCart(cart);
                        }
                        case 5 -> {
                            CheckoutService checkoutService=context.getBean(CheckoutService.class);
                            int cartId = inputHelper.readInt("Enter cart ID: ");
                            ShoppingCart cart=findCart(cartId);
                            Governorate governorate=selectGovernorate();
                            checkoutService.placeOrder(cart,governorate);

                        }
                        case 0 -> System.out.println("Ciao!");
                        default -> System.out.println("Invalid option.");
                    }
                } catch (StoreException e) {
                    System.out.println(e.getMessage());
                }

            } while (choice != 0);
        }
//        ProductService productService = context.getBean(ProductService.class);
//
//        System.out.println(productService);
//
//        Product product = Product.builder()
//                .price(BigDecimal.valueOf(100))
//                .build();
//
//        System.out.println(productService.getPrice("PH-X5-128", 2));
    }
    private static void showMenu() {
        System.out.println("========== NILEMART ==========");
        System.out.println("1. Show products");
        System.out.println("2. Create a cart");
        System.out.println("3. Add a product to a cart");
        System.out.println("4. Show a cart");
        System.out.println("5. Check out a cart");
        System.out.println("0. Exit");
    }
    private static void addProductToCart() {

        int cartId = inputHelper.readInt("Enter cart ID: ");

        ShoppingCart cart=findCart(cartId);

        String sku = inputHelper.readString("Enter product SKU: ");
        int quantity = inputHelper.readInt("Enter quantity: ");

        cart.addItem(sku, quantity);

        System.out.println("Product added successfully.");
    }
    private static void viewCart(ShoppingCart cart) {

        System.out.println("\n========== CART " + cart.getCartNumber() + " ==========");
        System.out.println("Customer: " + cart.getCustomer().fullName());

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Items:");

        cart.getLines().forEach((sku, qty) ->
                System.out.println("SKU: " + sku + " | Quantity: " + qty)
        );

        System.out.println("Subtotal: " + cart.subtotal() + " EGP");
    }
    private static ShoppingCart findCart(int cartId){
        ShoppingCart cart=carts.get(cartId);
        if(cart==null) {
            throw new StoreException("Cart not found.");
        }
        return cart;
    }
    private static Governorate selectGovernorate() {

        Governorate[] governorates = Governorate.values();

        System.out.println("\nSelect your governorate:");

        for (int i = 0; i < governorates.length; i++) {
            System.out.println((i + 1) + ". " + governorates[i]);
        }

        while (true) {
            int choice = inputHelper.readInt("Choose: ");

            if (choice >= 1 && choice <= governorates.length) {
                return governorates[choice - 1];
            }

            System.out.println("Invalid choice. Please try again.");
        }
    }
}
