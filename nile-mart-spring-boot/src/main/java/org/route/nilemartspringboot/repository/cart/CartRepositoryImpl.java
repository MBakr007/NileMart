package org.route.nilemartspringboot.repository.cart;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.route.nilemartspringboot.enums.AccountStatus;
import org.route.nilemartspringboot.enums.Role;
import org.route.nilemartspringboot.exception.CartException;
import org.route.nilemartspringboot.model.Cart;
import org.route.nilemartspringboot.model.CartItem;
import org.route.nilemartspringboot.model.Customer;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.repository.product.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Repository
public class CartRepositoryImpl implements CartRepository {

    // PURPOSE: ONLY TO USE THE GENERATED RANDOM PRODUCTS
    // DON'T DO THAT AT HOME
    private final ProductRepository productRepository;
    public CartRepositoryImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    private final List<Cart> carts = new ArrayList<>();



    @PostConstruct
    public void initCarts() {

        Faker faker = new Faker();
        Random random = new Random();
        List<Product> products = productRepository.findAll();


        for (int i = 0; i < 20; i++) {

            String cartId = UUID.randomUUID().toString();

            Customer customer = Customer.builder()
                    .id(UUID.randomUUID().toString())
                    .name(faker.name().fullName())
                    .hashPassword(UUID.randomUUID().toString())
                    .role(Role.CUSTOMER)
                    .accountStatus(random.nextBoolean()
                            ? AccountStatus.ACTIVE
                            : AccountStatus.BLOCKED)
                    .email(faker.internet().emailAddress())
                    .phone(faker.phoneNumber().phoneNumber())
                    .build();

            List<CartItem> cartItems = new ArrayList<>();

            int itemsCount = random.nextInt(5) + 1;

            for (int j = 0; j < itemsCount; j++) {

                Product product = products.get(
                        random.nextInt(products.size()));

                int quantity = random.nextInt(5) + 1;

                BigDecimal lineTotal = product.getPrice()
                        .multiply(BigDecimal.valueOf(quantity));

                CartItem cartItem = CartItem.builder()
                        .id(UUID.randomUUID().toString())
                        .cartId(cartId)
                        .product(product)
                        .quantity(quantity)
                        .addedAt(LocalDateTime.now()
                                .minusDays(random.nextInt(30)))
                        .lineTotal(lineTotal)
                        .build();

                cartItems.add(cartItem);
            }

            Cart cart = Cart.builder()
                    .id(cartId)
                    .customer(customer)
                    .cartItems(cartItems)
                    .build();

            carts.add(cart);
        }
    }

    @Override
    public Cart getCart(String customerId) {
        return getCartByCustomerId(customerId);
    }

    @Override
    public void addItem(CartItem cartItem, String customerId) {
        getCartByCustomerId(customerId).getCartItems().add(cartItem);
    }

    private Cart getCartByCustomerId(String customerId) {
        return carts.stream()
                .filter(cart -> cart.getCustomer().getId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CartException("No cart is assigned to customer with id: " + customerId, HttpStatus.NOT_FOUND));
    }
}
