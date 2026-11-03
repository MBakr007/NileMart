package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //        var context = new ClassPathXmlApplicationContext("beans.xml");


//        StandardPricingService standard = context.getBean(StandardPricingService.class);
//        StandardPricingService standard2 = context.getBean(StandardPricingService.class);

//        ProductService p = context.getBean(ProductService.class);
//
//        System.out.println(p);

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        ProductService productService = context.getBean(ProductService.class);
        CheckoutService checkoutService = context.getBean(CheckoutService.class);

        Scanner scanner = new Scanner(System.in);
        Map<Integer, ShoppingCart> carts = new HashMap<>();

        int cartCounter = 0;
        int choice;
        do {

            System.out.println("""
            ========== NILEMART ==========
            1. Show products
            2. Create a cart
            3. Add a product to a cart
            4. Show a cart
            5. Check out a cart
            0. Exit
            """);

            choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1 -> {

                        //showing all products
                        for (Product p : productService.findAll()) {
                            System.out.println(p);
                        }
                    }

                    case 2 -> {

                        //getting a new cart from spring
                        ShoppingCart cart = context.getBean(ShoppingCart.class);

                        cartCounter++;
                        cart.setCartNumber(cartCounter);

                        System.out.print("Customer name: ");
                        String name = scanner.nextLine();

                        System.out.print("Phone: ");
                        String phone = scanner.nextLine();

                        System.out.print("Email: ");
                        String email = scanner.nextLine();

                        Customer customer =
                                new Customer(name, phone, email);

                        cart.setCustomer(customer);

                        carts.put(cartCounter, cart);

                        System.out.println(
                                "Cart created. Number: " + cartCounter
                        );
                    }

                    case 3 -> {

                        System.out.print("Cart number: ");
                        int cartNumber = scanner.nextInt();
                        scanner.nextLine();

                        ShoppingCart cart = carts.get(cartNumber);

                        if (cart == null) {
                            throw new StoreException("Cart not found.");
                        }

                        System.out.print("Product sku: ");
                        String sku = scanner.nextLine();

                        System.out.print("Quantity: ");
                        int qty = scanner.nextInt();
                        scanner.nextLine();

                        cart.addItem(sku, qty);
                    }

                    case 4 -> {

                        System.out.print("Cart number: ");
                        int cartNumber = scanner.nextInt();
                        scanner.nextLine();

                        ShoppingCart cart = carts.get(cartNumber);

                        if (cart == null) {
                            throw new StoreException("Cart not found.");
                        }

                        System.out.println(cart.getLines());
                        System.out.println("Subtotal: " + cart.subtotal());
                    }

                    case 5 -> {

                        System.out.print("Cart number: ");
                        int cartNumber = scanner.nextInt();
                        scanner.nextLine();

                        ShoppingCart cart = carts.get(cartNumber);

                        if (cart == null) {
                            throw new StoreException("Cart not found.");
                        }

                        System.out.print("Governorate: ");
                        String governorateName = scanner.nextLine();

                        Governorate governorate =
                                Governorate.valueOf(
                                        governorateName.toUpperCase()
                                );

                        Order order =
                                checkoutService.placeOrder(
                                        cart,
                                        governorate
                                );

                        System.out.println(
                                "Order confirmed: " + order.orderNumber()
                        );
                    }
                }

            } catch (StoreException e) {

                //the error doesnt close the program
                System.out.println(e.getMessage());
            }

        } while (choice != 0);
        scanner.close();
        context.close();
        //TASK TEST END


//        var context2 = new AnnotationConfigApplicationContext(AppConfig.class);

//        StandardPricingService standard = context.getBean(StandardPricingService.class);
//        StandardPricingService standard2 = context2.getBean(StandardPricingService.class);

//        System.out.println(standard == standard2);
//        ProductService p = context.getBean(ProductService.class);
//        System.out.println(standard);

       // ProductService productService = context.getBean(ProductService.class);

      //  System.out.println(productService);

     //   Product product = Product.builder()
       //         .price(BigDecimal.valueOf(100))
       //         .build();

       // System.out.println(productService.getPrice("PH-X5-128", 2));


    }
}
