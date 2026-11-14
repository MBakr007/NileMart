package org.route.nilemartspringboot.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotBlank
    private String productId;
    @Positive
    @NotNull
    private Integer quantity;
    @NotBlank
    private String customerId;
}
