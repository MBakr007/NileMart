package org.route.nilemartspringboot.controller;


import jakarta.validation.Valid;
import org.route.nilemartspringboot.dto.cart.CartItemRequest;
import org.route.nilemartspringboot.dto.cart.CartResponse;
import org.route.nilemartspringboot.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cart")
    public ResponseEntity<?> addItem(@RequestBody @Valid CartItemRequest cartItemRequest) {
        cartService.addItem(cartItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
