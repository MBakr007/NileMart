package org.route.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryRow {

    private final String name;
    private final String slug;
    private final String parentName;
    private final Integer displayOrder;
    private final boolean active;
}
