package com.teju.flashsale.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderHistoryResponse {
    private Long orderId;
    private String productName;
    private int quantity;
    private String status;
    private LocalDateTime createdAt;
}