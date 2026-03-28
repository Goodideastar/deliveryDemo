package com.warehouse.deliverydemo.mapper;

import com.warehouse.deliverydemo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insert(Order order);
    Order findById(@Param("id") Long id);
    Order findByOrderNo(@Param("orderNo") String orderNo);
    List<Order> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    int countByUserId(@Param("userId") Long userId);
    List<Order> findByMerchantId(@Param("merchantId") Long merchantId, @Param("offset") int offset, @Param("limit") int limit);
    int countByMerchantId(@Param("merchantId") Long merchantId);
    void update(Order order);
}