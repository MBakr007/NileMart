package org.route.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    @NotBlank(message = "SKU can't be empty.")
    private String sku;
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @Positive(message = "price must be greater than zero.")
    private BigDecimal price;
    @PositiveOrZero
    private Integer inStock;
}
