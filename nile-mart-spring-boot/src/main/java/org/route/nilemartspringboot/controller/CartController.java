package org.route.nilemartspringboot.controller;


import org.route.nilemartspringboot.dto.cart.CartResponse;
import org.route.nilemartspringboot.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

//    @PostMapping("/cart")
//    public ResponseEntity<?> addItem(@RequestBody @Valid CartItemRequest cartItemRequest) {
//        cartService.addItem(cartItemRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
}
