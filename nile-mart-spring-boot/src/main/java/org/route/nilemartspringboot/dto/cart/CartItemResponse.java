package org.route.nilemartspringboot.dto.cart;

import lombok.Builder;
import lombok.Data;
import org.route.nilemartspringboot.dto.product.ProductResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CartItemResponse {
    private String id;
    private ProductResponse product;
    private Integer quantity;
    private LocalDateTime addedAt;
    private BigDecimal lineTotal;
}
