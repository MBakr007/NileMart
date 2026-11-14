package org.route.nilemartspringboot.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryRequest {
    private String id;
    private String name;
    private String slug;
    private boolean active;
}
