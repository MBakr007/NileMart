package org.route.nilemartspringboot.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String sku;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer inStock;
    private Boolean active;
}
