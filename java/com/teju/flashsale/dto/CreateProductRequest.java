package com.teju.flashsale.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private double price;
    private int initialStock;
}