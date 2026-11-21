package com.eic.product_catalogue_api.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;
    @Column(nullable = false, precision =  12, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private Long quantity;
    private String description;
}
