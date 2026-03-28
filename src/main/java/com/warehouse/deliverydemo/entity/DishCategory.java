package com.warehouse.deliverydemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DishCategory {
    private Long id;
    private Long merchantId;
    private String name;
    private Integer sort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}