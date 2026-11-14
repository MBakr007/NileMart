package org.route.nilemartspringboot.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AddProductRequest {
    @NotBlank
    private String sku;
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @Positive
    private BigDecimal price;
    @PositiveOrZero
    private Integer inStock;
    @NotNull
    private Boolean active;
}
