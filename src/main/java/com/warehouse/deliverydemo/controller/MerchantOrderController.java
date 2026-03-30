package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.common.Result;
import com.warehouse.deliverydemo.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant/order")
public class MerchantOrderController {
    private static final Logger logger = LoggerFactory.getLogger(MerchantOrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Result<?> list(@RequestParam Long merchantId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("查询商家订单列表，商家ID: {}, 页码: {}, 每页大小: {}", merchantId, page, size);
        try {
            Map<String, Object> data = orderService.findByMerchantId(merchantId, page, size);
            logger.info("查询商家订单列表成功，商家ID: {}, 总数量: {}", merchantId, data.get("total"));
            return Result.success(data);
        } catch (Exception e) {
            logger.error("查询商家订单列表失败，商家ID: {}", merchantId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<?> get(@PathVariable Long id) {
        logger.info("查询订单详情，订单ID: {}", id);
        try {
            Map<String, Object> order = orderService.findById(id);
            if (order != null) {
                logger.info("查询订单详情成功，订单ID: {}", id);
                return Result.success(order);
            } else {
                logger.warn("订单不存在，订单ID: {}", id);
                return Result.notFound("订单不存在");
            }
        } catch (Exception e) {
            logger.error("查询订单详情失败，订单ID: {}", id, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/items/{orderId}")
    public Result<?> getOrderItems(@PathVariable Long orderId) {
        logger.info("查询订单商品，订单ID: {}", orderId);
        try {
            Object order = orderService.findById(orderId);
            if (order != null) {
                List<Map<String, Object>> items = orderService.findOrderItems(orderId);
                logger.info("查询订单商品成功，订单ID: {}", orderId);
                return Result.success(items);
            } else {
                logger.warn("订单不存在，订单ID: {}", orderId);
                return Result.notFound("订单不存在");
            }
        } catch (Exception e) {
            logger.error("查询订单商品失败，订单ID: {}", orderId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id,
            @RequestParam String status) {
        logger.info("更新订单状态，订单ID: {}, 新状态: {}", id, status);
        try {
            orderService.updateOrderStatus(id, status);
            logger.info("更新订单状态成功，订单ID: {}, 新状态: {}", id, status);
            return Result.success("状态更新成功");
        } catch (Exception e) {
            logger.error("更新订单状态失败，订单ID: {}, 新状态: {}", id, status, e);
            return Result.serverError(e.getMessage());
        }
    }
}