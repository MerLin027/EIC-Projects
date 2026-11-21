package com.eic.product_catalogue_api.product.service;

import com.eic.product_catalogue_api.product.dto.CategoryRequest;
import com.eic.product_catalogue_api.product.dto.CategoryResponse;
import com.eic.product_catalogue_api.product.entity.Category;
import com.eic.product_catalogue_api.product.exception.DuplicateResourceException;
import com.eic.product_catalogue_api.product.mapper.CategoryMapper;
import com.eic.product_catalogue_api.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService
{
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    //
    @Transactional
    public CategoryResponse create(CategoryRequest request)
    {
        if(repository.existsByName(request.name()))
            throw new DuplicateResourceException("Category " + request.name() + " already exists");
        Category category = mapper.toEntity(request);
        return mapper.toResponse(repository.save(category));
    }
    //
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll()
    {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }
}
