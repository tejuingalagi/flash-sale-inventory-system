package com.teju.flashsale.service.impl;

import com.teju.flashsale.dto.CreateProductRequest;
import com.teju.flashsale.dto.ProductResponse;
import com.teju.flashsale.entity.Inventory;
import com.teju.flashsale.entity.Product;
import com.teju.flashsale.repository.InventoryRepository;
import com.teju.flashsale.repository.ProductRepository;
import com.teju.flashsale.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                               InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        Product savedProduct = productRepository.save(product);

        Inventory inventory = Inventory.builder()
                .productId(savedProduct.getId())
                .availableStock(request.getInitialStock())
                .build();
        inventoryRepository.save(inventory);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                request.getInitialStock()
        );
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> {
                    Inventory inventory = inventoryRepository.findByProductId(product.getId())
                            .orElse(null);
                    int stock = (inventory != null) ? inventory.getAvailableStock() : 0;
                    return new ProductResponse(product.getId(), product.getName(), product.getPrice(), stock);
                })
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, CreateProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setAvailableStock(request.getInitialStock());

        inventoryRepository.save(inventory);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                inventory.getAvailableStock()
        );
    }
}