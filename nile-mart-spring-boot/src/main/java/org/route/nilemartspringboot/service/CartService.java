package org.route.nilemartspringboot.service;

import org.route.nilemartspringboot.properties.StoreProperties;
import org.route.nilemartspringboot.dto.cart.CartResponse;
import org.route.nilemartspringboot.mapper.CartMapper;
import org.route.nilemartspringboot.repository.cart.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartMapper mapper;
    private final StoreProperties properties;
    private static final Logger log =  LoggerFactory.getLogger(CartService.class);

    public CartService(CartRepository cartRepository, ProductService productService, CartMapper mapper, StoreProperties properties) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.mapper = mapper;
        this.properties = properties;
    }

    public CartResponse getCart(String customerId) {
        return mapper.toCartResponse(cartRepository.getCart(customerId));
    }


//    public void addItem(CartItemRequest request) {
//
//        log.info("start getting product with id: {}", request.getProductId());
//        Product product = productService.getProductById(request.getProductId());
//
//        if (product != null) {
//            log.info("adding product with id: {}", request.getProductId());
//            if (product.getActive()
//                    && product.getInStock() > request.getQuantity()
//                    && properties.getMaxProductQuantity() >= request.getQuantity()) {
//                CartItem cartItem = CartItem.builder()
//                        .id("1")
//                        .product(product)
//                        .quantity(request.getQuantity())
//                        .addedAt(LocalDateTime.now())
//                        .build();
//
//                try {
//                    cartRepository.addItem(cartItem, request.getCustomerId());
//                } catch (CartException e) {
//                    log.error("failed adding product with id: {} to your cart for customer with id {}",
//                            request.getProductId(), request.getCustomerId(), e);
//                    throw e;
//                }
//            }
//        }
//    }
}
