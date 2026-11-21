package com.eic.product_catalogue_api.product.repository;

import com.eic.product_catalogue_api.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    boolean existsByName(String name);
}
