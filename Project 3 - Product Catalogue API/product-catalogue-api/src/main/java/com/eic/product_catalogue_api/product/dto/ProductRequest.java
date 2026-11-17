package com.eic.product_catalogue_api.product.dto;

import com.eic.product_catalogue_api.product.entity.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequest(@NotBlank(message = "Name is required")
                             String name,
                             @NotBlank(message = "Category is required")
                             @Valid
                             CategoryRequest category,
                             @NotBlank(message = "Price is required")
                             @PositiveOrZero(message = "Price should be >= 0")
                             BigDecimal price,
                             @NotBlank(message = "Quantity is required")
                             @PositiveOrZero(message = "Quantity should be >= 0")
                             Long quantity,
                             String description)
{
}
