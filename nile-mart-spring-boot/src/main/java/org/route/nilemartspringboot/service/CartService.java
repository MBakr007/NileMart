package org.route.nilemartspringboot.service;

import org.route.nilemartspringboot.config.StoreProperties;
import org.route.nilemartspringboot.dto.cart.CartItemRequest;
import org.route.nilemartspringboot.dto.cart.CartResponse;
import org.route.nilemartspringboot.mapper.CartMapper;
import org.route.nilemartspringboot.model.CartItem;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.repository.cart.CartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartMapper mapper;
    private final StoreProperties properties;

    public CartService(CartRepository cartRepository, ProductService productService, CartMapper mapper, StoreProperties properties) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.mapper = mapper;
        this.properties = properties;
    }

    public CartResponse getCart(String customerId) {
        return mapper.toCartResponse(cartRepository.getCart(customerId));
    }


    public void addItem(CartItemRequest request) {

        Product product = productService.getProductById(request.getProductId());

        if (product != null) {
            if (product.getActive()
                    && product.getInStock() > request.getQuantity()
                    && properties.getMaxProductQuantity() >= request.getQuantity()) {
                CartItem cartItem = CartItem.builder()
                        .id("1")
                        .product(product)
                        .quantity(request.getQuantity())
                        .addedAt(LocalDateTime.now())
                        .build();
                cartRepository.addItem(cartItem, request.getCustomerId());
            }
        }
    }
}
