package com.teju.flashsale.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String name;
    private double price;
    private int availableStock;
}