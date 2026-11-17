package com.eic.product_catalogue_api.product.mapper;

import com.eic.product_catalogue_api.product.dto.CategoryRequest;
import com.eic.product_catalogue_api.product.dto.CategoryResponse;
import com.eic.product_catalogue_api.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface CategoryMapper
{
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest request);
    //
    CategoryResponse toResponse(Category category);
    //
    @Mapping(target = "id", ignore = true)
    Category updateEntity(CategoryRequest request, @MappingTarget Category category);
}
