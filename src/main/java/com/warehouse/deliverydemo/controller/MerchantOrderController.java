package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant/order")
public class MerchantOrderController {
    private static final Logger logger = LoggerFactory.getLogger(MerchantOrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Map<String, Object> list(@RequestParam Long merchantId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("查询商家订单列表，商家ID: {}, 页码: {}, 每页大小: {}", merchantId, page, size);
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = orderService.findByMerchantId(merchantId, page, size);
            logger.info("查询商家订单列表成功，商家ID: {}, 总数量: {}", merchantId, data.get("total"));
            result.put("code", 200);
            result.put("data", data);
        } catch (Exception e) {
            logger.error("查询商家订单列表失败，商家ID: {}", merchantId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable Long id) {
        logger.info("查询订单详情，订单ID: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> order = orderService.findById(id);
            if (order != null) {
                logger.info("查询订单详情成功，订单ID: {}", id);
                result.put("code", 200);
                result.put("data", order);
            } else {
                logger.warn("订单不存在，订单ID: {}", id);
                result.put("code", 404);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            logger.error("查询订单详情失败，订单ID: {}", id, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/items/{orderId}")
    public Map<String, Object> getOrderItems(@PathVariable Long orderId) {
        logger.info("查询订单商品，订单ID: {}", orderId);
        Map<String, Object> result = new HashMap<>();
        try {
            Object order = orderService.findById(orderId);
            if (order != null) {
                List<Map<String, Object>> items = orderService.findOrderItems(orderId);
                logger.info("查询订单商品成功，订单ID: {}", orderId);
                result.put("code", 200);
                result.put("data", items);
            } else {
                logger.warn("订单不存在，订单ID: {}", orderId);
                result.put("code", 404);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            logger.error("查询订单商品失败，订单ID: {}", orderId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/status/{id}")
    public Map<String, Object> updateStatus(@PathVariable Long id,
            @RequestParam String status) {
        logger.info("更新订单状态，订单ID: {}, 新状态: {}", id, status);
        Map<String, Object> result = new HashMap<>();
        try {
            orderService.updateOrderStatus(id, status);
            logger.info("更新订单状态成功，订单ID: {}, 新状态: {}", id, status);
            result.put("code", 200);
            result.put("message", "状态更新成功");
        } catch (Exception e) {
            logger.error("更新订单状态失败，订单ID: {}, 新状态: {}", id, status, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}