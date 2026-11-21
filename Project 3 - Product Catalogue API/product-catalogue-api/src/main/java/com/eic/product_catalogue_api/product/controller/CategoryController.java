package com.eic.product_catalogue_api.product.controller;

import com.eic.product_catalogue_api.product.dto.CategoryRequest;
import com.eic.product_catalogue_api.product.dto.CategoryResponse;
import com.eic.product_catalogue_api.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories")
public class CategoryController
{
    private final CategoryService service;
    // POST /categories
    @PostMapping
    @Operation(summary = "Creates a category")
    ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request)
    {
        CategoryResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // GET /categories
    @GetMapping
    @Operation(summary = "Retrieves all categories")
    ResponseEntity <List<CategoryResponse>> getAllCategories()
    {
        return ResponseEntity.ok(service.findAll());
    }
}