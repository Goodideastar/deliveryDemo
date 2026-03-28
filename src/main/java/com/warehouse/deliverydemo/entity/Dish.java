package com.warehouse.deliverydemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Dish {
    private Long id;
    private Long merchantId;
    private Long categoryId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;
    private Integer status;
    private Integer sort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}