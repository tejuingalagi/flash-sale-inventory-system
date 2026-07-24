package com.teju.flashsale.controller;

import com.teju.flashsale.dto.OrderResponse;
import com.teju.flashsale.dto.PurchaseRequest;
import com.teju.flashsale.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<OrderResponse> purchaseItem(@RequestBody PurchaseRequest request) {

        // TEMPORARY: hardcoded userId for testing, until JWT security is added in Step 7
        Long tempUserId = 1L;

        OrderResponse response = orderService.purchaseItem(request, tempUserId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}