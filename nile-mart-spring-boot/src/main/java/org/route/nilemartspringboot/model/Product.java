package org.route.nilemartspringboot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String sku;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer inStock;
    private Boolean active;
}
