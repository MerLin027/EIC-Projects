package com.eic.product_catalogue_api.product.controller;

import com.eic.product_catalogue_api.product.dto.ProductRequest;
import com.eic.product_catalogue_api.product.dto.ProductResponse;
import com.eic.product_catalogue_api.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products")
public class ProductController
{
    private final ProductService service;
    // POST /products
    @PostMapping
    @Operation(summary = "Creates products")
    ResponseEntity <ProductResponse> createProduct(@Valid @RequestBody ProductRequest request)
    {
        ProductResponse response = service.create(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // GET /products
    @GetMapping
    @Operation(summary = "Retrieves all products")
    ResponseEntity <PagedModel<ProductResponse>> getAllProducts(@PageableDefault(size = 3, sort = "id")
                                                                Pageable pageable)
    {
        return ResponseEntity.ok().body(new PagedModel<>(service.findAll(pageable)));
    }
    // GET /products/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Retrieves a product using its ID")
    ResponseEntity <ProductResponse> getProductById(@PathVariable Long id)
    {
        ProductResponse response = service.findById(id);
        return  ResponseEntity.ok(response);
    }
    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a product using its ID")
    ResponseEntity <Void> deleteProductById(@PathVariable Long id)
    {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    // PUT /products/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Updates an existing product")
    ResponseEntity <ProductResponse> updateProductById(@PathVariable Long id, @Valid @RequestBody ProductRequest request)
    {
        ProductResponse response = service.updateById(id, request);
        return   ResponseEntity.ok().body(response);
    }
}
