package org.route.nilemartspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private String id;
    private String cartId;
    private Product product;
    private Integer quantity;
    private LocalDateTime addedAt;
    private BigDecimal lineTotal;
}
