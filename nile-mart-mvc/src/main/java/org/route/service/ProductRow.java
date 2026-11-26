package org.route.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductRow {

    private final String sku;
    private final String name;
    private final BigDecimal price;
    private final Integer inStock;
    private final String categoryName;
    private final String brandName;
}
