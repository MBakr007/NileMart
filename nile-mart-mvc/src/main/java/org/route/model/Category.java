package org.route.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Slug is required")
    @Pattern(regexp = "[a-z0-9-]+",
            message = "Slug must contain only lower-case letters, digits and hyphens")
    private String slug;

    //storing the parent slug, empty for a top level category
    private String parentSlug = "";

    @NotNull(message = "Display order is required")
    @Min(value = 0, message = "Display order must be 0 or more")
    private Integer displayOrder;

    private boolean active = true;
}
