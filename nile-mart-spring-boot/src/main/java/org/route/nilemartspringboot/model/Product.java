package org.route.nilemartspringboot.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,  length = 200)
    private String name;
    @Column(nullable = false,  length = 100)
    private String category;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false,  length = 10)
    private Integer inStock;
    @Column(nullable = false,  length = 100)
    private String active;
}
