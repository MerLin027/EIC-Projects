package com.eic.product_catalogue_api.product.mapper;

import com.eic.product_catalogue_api.product.dto.ProductRequest;
import com.eic.product_catalogue_api.product.dto.ProductResponse;
import com.eic.product_catalogue_api.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper
{
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequest request);
    //
    ProductResponse toResponse(Product product);
    //
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category",  ignore = true)
    Product updateEntity(ProductRequest updatedProduct, @MappingTarget Product product);
}
