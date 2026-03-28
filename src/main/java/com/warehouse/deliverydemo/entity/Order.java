package com.warehouse.deliverydemo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private String userName;
    private Long merchantId;
    private Long addressId;
    private Double totalAmount;
    private String status;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private String shippingAddress;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> orderItems;
}