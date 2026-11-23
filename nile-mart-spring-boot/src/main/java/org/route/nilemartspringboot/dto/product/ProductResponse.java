package org.route.nilemartspringboot.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Integer id;
    private String name;
    private String category;
    private BigDecimal price;
}
