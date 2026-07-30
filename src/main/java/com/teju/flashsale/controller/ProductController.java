package com.teju.flashsale.controller;

import com.teju.flashsale.dto.CreateProductRequest;
import com.teju.flashsale.dto.ProductResponse;
import com.teju.flashsale.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Admin: Create Product
    @PostMapping("/admin/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Admin: View All Products
    @GetMapping("/admin/products")
    public ResponseEntity<List<ProductResponse>> getAllProductsAdmin() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    // Public: View Products
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getPublicProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    
    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request) {

        ProductResponse response = productService.updateProduct(id, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}