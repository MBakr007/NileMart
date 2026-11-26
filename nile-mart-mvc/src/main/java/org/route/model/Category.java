package org.route.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Slug is required")
    @Pattern(regexp = "^[a-z0-9-]*$",
            message = "Slug may contain only lower-case letters, digits and hyphens")
    private String slug;

    private String parentSlug;

    @NotNull(message = "Display order is required")
    @PositiveOrZero(message = "Display order must be zero or greater")
    private Integer displayOrder;

    @Builder.Default
    private boolean active = true;
}
