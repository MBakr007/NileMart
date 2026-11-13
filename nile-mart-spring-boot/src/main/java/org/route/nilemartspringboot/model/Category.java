package org.route.nilemartspringboot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private String id;
    private String name;
    private String slug;
    private boolean active;
}
