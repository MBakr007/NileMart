package org.route.nilemartspringboot.dto.category;


import lombok.Data;

@Data
public class CategoryResponse {
    private Integer id;
    private String name;
    private String slug;
    private Boolean active;
}
