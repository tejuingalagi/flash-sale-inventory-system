package com.teju.flashsale.service;

import com.teju.flashsale.dto.OrderHistoryResponse;
import com.teju.flashsale.dto.OrderResponse;
import com.teju.flashsale.dto.PurchaseRequest;

import java.util.List;

public interface OrderService {
    OrderResponse purchaseItem(PurchaseRequest request, String email);
    List<OrderHistoryResponse> getMyOrders(String email);
}