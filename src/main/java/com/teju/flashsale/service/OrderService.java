package com.teju.flashsale.service;

import com.teju.flashsale.dto.OrderResponse;
import com.teju.flashsale.dto.PurchaseRequest;

public interface OrderService {
    OrderResponse purchaseItem(PurchaseRequest request, Long userId);
}