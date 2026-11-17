package com.eic.product_catalogue_api.product.repository;

import com.eic.product_catalogue_api.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long>
{
    Category findByName(String name);
    boolean existsByName(String name);
}
