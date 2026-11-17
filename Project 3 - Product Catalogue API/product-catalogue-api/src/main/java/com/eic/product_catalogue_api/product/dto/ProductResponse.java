package com.eic.product_catalogue_api.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponse(Long id,
                              String name,
                              CategoryResponse category,
                              BigDecimal price,
                              Long quantity,
                              String description)
{
}
