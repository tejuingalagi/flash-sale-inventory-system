package com.teju.flashsale.service;

import com.teju.flashsale.dto.CreateProductRequest;
import com.teju.flashsale.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(Long productId, CreateProductRequest request);
}