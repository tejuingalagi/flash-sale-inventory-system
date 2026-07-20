package com.teju.flashsale.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private String status;   // "CONFIRMED" or "SOLD_OUT"
    private String message;
    private Long orderId;
}