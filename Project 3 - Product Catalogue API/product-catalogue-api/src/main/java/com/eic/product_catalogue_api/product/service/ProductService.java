package com.eic.product_catalogue_api.product.service;

import com.eic.product_catalogue_api.product.dto.ProductRequest;
import com.eic.product_catalogue_api.product.dto.ProductResponse;
import com.eic.product_catalogue_api.product.entity.Category;
import com.eic.product_catalogue_api.product.entity.Product;
import com.eic.product_catalogue_api.product.exception.ResourceNotFoundException;
import com.eic.product_catalogue_api.product.mapper.ProductMapper;
import com.eic.product_catalogue_api.product.repository.CategoryRepository;
import com.eic.product_catalogue_api.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    //
    @Transactional
    public ProductResponse create(ProductRequest request)
    {
        Product product = mapper.toEntity(request);
        return  mapper.toResponse(productRepository.save(product));
    }
    //
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable)
    {
        return productRepository.findAll(pageable).map(mapper::toResponse);
    }
    //
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) throws ResourceNotFoundException
    {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product with id "+id+" not found"));
        return mapper.toResponse(product);
    }
    //
    public void deleteById(Long id) throws ResourceNotFoundException
    {
        if(!productRepository.existsById(id))
            throw new ResourceNotFoundException("Product with id "+id+" not found");
        productRepository.deleteById(id);
    }
    //
    @Transactional
    public ProductResponse updateById(Long id, ProductRequest request) throws ResourceNotFoundException
    {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product with id "+id+" not found"));
        Category category = findCategory(request);
        product = mapper.updateEntity(request, product);
        product.setCategory(category);
        return mapper.toResponse(productRepository.save(product));
    }
    private Category findCategory(ProductRequest request) throws ResourceNotFoundException
    {
        String name = request.category().name();
        return categoryRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Category "+name+" not found"));
    }
}