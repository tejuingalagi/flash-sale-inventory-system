package com.teju.flashsale.service.impl;

import com.teju.flashsale.dto.OrderResponse;
import com.teju.flashsale.dto.PurchaseRequest;
import com.teju.flashsale.entity.Inventory;
import com.teju.flashsale.entity.Order;
import com.teju.flashsale.exception.OutOfStockException;
import com.teju.flashsale.repository.InventoryRepository;
import com.teju.flashsale.repository.OrderRepository;
import com.teju.flashsale.service.OrderService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    // Constructor injection - Spring automatically provides these beans
    public OrderServiceImpl(InventoryRepository inventoryRepository,
                             OrderRepository orderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderResponse purchaseItem(PurchaseRequest request, Long userId) {

        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found in inventory"));

        if (inventory.getAvailableStock() < request.getQuantity()) {
            throw new OutOfStockException("Sold out! Not enough stock available.");
        }

        try {
            inventory.setAvailableStock(inventory.getAvailableStock() - request.getQuantity());
            inventoryRepository.save(inventory);  // version check happens here automatically

            Order order = Order.builder()
                    .userId(userId)
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .status("CONFIRMED")
                    .createdAt(LocalDateTime.now())
                    .build();

            Order savedOrder = orderRepository.save(order);

            return new OrderResponse("CONFIRMED", "Order placed successfully!", savedOrder.getId());

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OutOfStockException("Sold out! Someone just bought the last item.");
        }
    }
}