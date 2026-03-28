package com.warehouse.deliverydemo.mapper;

import com.warehouse.deliverydemo.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    void insert(OrderItem orderItem);
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);
}