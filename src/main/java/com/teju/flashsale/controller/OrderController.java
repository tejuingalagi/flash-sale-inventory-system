package com.teju.flashsale.controller;

import com.teju.flashsale.dto.OrderResponse;
import com.teju.flashsale.dto.PurchaseRequest;
import com.teju.flashsale.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<OrderResponse> purchaseItem(@RequestBody PurchaseRequest request,
                                                         Authentication authentication) {
        String email = authentication.getName();
        // We'll look up the actual userId from email inside the service now
        OrderResponse response = orderService.purchaseItem(request, email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}