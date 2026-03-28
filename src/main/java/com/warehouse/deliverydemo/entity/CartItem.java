package com.warehouse.deliverydemo.entity;

import lombok.Data;

@Data
public class CartItem {
    private Long dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
    private String image;
    private Long merchantId;
}