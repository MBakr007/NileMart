package org.route.nilemartspringboot.dto.category;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoryResponse {
    private String id;
    private String name;
    private String slug;
    private Boolean isActive;
    private BigDecimal price;
}
