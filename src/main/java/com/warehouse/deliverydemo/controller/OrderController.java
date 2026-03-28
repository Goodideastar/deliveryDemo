package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.service.OrderService;
import com.warehouse.deliverydemo.service.PayService;
import com.warehouse.deliverydemo.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestAttribute("userId") Long userId,
            @RequestParam Long merchantId,
            @RequestParam Long addressId,
            @RequestParam(required = false) String remark) {
        logger.info("用户创建订单，用户ID: {}, 商家ID: {}, 地址ID: {}, 备注: {}", userId, merchantId, addressId, remark);
        Map<String, Object> result = new HashMap<>();
        try {
            Object order = orderService.createOrder(userId, merchantId, addressId, remark);
            logger.info("用户创建订单成功，用户ID: {}", userId);
            result.put("code", 200);
            result.put("message", "订单创建成功");
            result.put("data", order);
        } catch (Exception e) {
            logger.error("用户创建订单失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping
    public Map<String, Object> list(@RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("用户查询订单列表，用户ID: {}, 页码: {}, 每页大小: {}", userId, page, size);
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = orderService.findByUserId(userId, page, size);
            logger.info("用户查询订单列表成功，用户ID: {}, 总数量: {}", userId, data.get("total"));
            result.put("code", 200);
            result.put("data", data);
        } catch (Exception e) {
            logger.error("用户查询订单列表失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        logger.info("用户查询订单详情，用户ID: {}, 订单ID: {}", userId, id);
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> order = orderService.findById(id);
            if (order != null) {
                logger.info("用户查询订单详情成功，用户ID: {}, 订单ID: {}", userId, id);
                result.put("code", 200);
                result.put("data", order);
            } else {
                logger.warn("订单不存在，用户ID: {}, 订单ID: {}", userId, id);
                result.put("code", 404);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            logger.error("用户查询订单详情失败，用户ID: {}, 订单ID: {}", userId, id, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/items/{orderId}")
    public Map<String, Object> getOrderItems(@PathVariable Long orderId, @RequestAttribute("userId") Long userId) {
        logger.info("用户查询订单商品，用户ID: {}, 订单ID: {}", userId, orderId);
        Map<String, Object> result = new HashMap<>();
        try {
            Object order = orderService.findById(orderId);
            if (order != null) {
                List<Map<String, Object>> items = orderService.findOrderItems(orderId);
                logger.info("用户查询订单商品成功，用户ID: {}, 订单ID: {}", userId, orderId);
                result.put("code", 200);
                result.put("data", items);
            } else {
                logger.warn("订单不存在，用户ID: {}, 订单ID: {}", userId, orderId);
                result.put("code", 404);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            logger.error("用户查询订单商品失败，用户ID: {}, 订单ID: {}", userId, orderId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/status/{id}")
    public Map<String, Object> updateStatus(@PathVariable Long id,
            @RequestParam String status,
            @RequestAttribute("userId") Long userId) {
        logger.info("用户更新订单状态，用户ID: {}, 订单ID: {}, 新状态: {}", userId, id, status);
        Map<String, Object> result = new HashMap<>();
        try {
            orderService.updateOrderStatus(id, status);
            logger.info("用户更新订单状态成功，用户ID: {}, 订单ID: {}, 新状态: {}", userId, id, status);
            result.put("code", 200);
            result.put("message", "状态更新成功");
        } catch (Exception e) {
            logger.error("用户更新订单状态失败，用户ID: {}, 订单ID: {}, 新状态: {}", userId, id, status, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/payment/callback")
    public Map<String, Object> paymentCallback(@RequestParam String orderNo,
            @RequestParam String paymentMethod) {
        logger.info("支付回调，订单号: {}, 支付方式: {}", orderNo, paymentMethod);
        Map<String, Object> result = new HashMap<>();
        try {
            orderService.handlePaymentCallback(orderNo, paymentMethod);
            logger.info("支付回调处理成功，订单号: {}", orderNo);
            result.put("code", 200);
            result.put("message", "支付成功");
        } catch (Exception e) {
            logger.error("支付回调处理失败，订单号: {}", orderNo, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/pay")
    public Map<String, Object> pay(@RequestAttribute("userId") Long userId,
            @RequestParam Long orderId,
            @RequestParam String paymentMethod) {
        logger.info("用户支付订单，用户ID: {}, 订单ID: {}, 支付方式: {}", userId, orderId, paymentMethod);
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取订单信息
            Map<String, Object> order = orderService.findById(orderId);
            if (order == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }

            // 检查订单状态
            if (!"pending".equals(order.get("status"))) {
                result.put("code", 400);
                result.put("message", "订单状态不正确，无法支付");
                return result;
            }

            // 检查订单所属用户
            if (!userId.equals(order.get("userId"))) {
                result.put("code", 403);
                result.put("message", "无权操作此订单");
                return result;
            }

            // 模拟支付处理
            // 实际项目中应该调用支付SDK或跳转到支付页面
            orderService.updateOrderStatus(orderId, "paid", paymentMethod);

            logger.info("用户支付订单成功，用户ID: {}, 订单ID: {}", userId, orderId);
            result.put("code", 200);
            result.put("message", "支付成功");
        } catch (Exception e) {
            logger.error("用户支付订单失败，用户ID: {}, 订单ID: {}", userId, orderId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/pay/wechat")
    public Map<String, Object> wechatPay(@RequestAttribute("userId") Long userId,
            @RequestParam Long orderId,
            @RequestParam String openId) {
        logger.info("用户微信支付，用户ID: {}, 订单ID: {}, OpenID: {}", userId, orderId, openId);
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取订单信息
            Map<String, Object> order = orderService.findById(orderId);
            if (order == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }

            // 检查订单状态
            if (!"pending".equals(order.get("status"))) {
                result.put("code", 400);
                result.put("message", "订单状态不正确，无法支付");
                return result;
            }

            // 检查订单所属用户
            if (!userId.equals(order.get("userId"))) {
                result.put("code", 403);
                result.put("message", "无权操作此订单");
                return result;
            }

            // 调用微信支付服务
            double totalAmount = (double) order.get("totalAmount");
            Long merchantId = (Long) order.get("merchantId");
            Map<String, Object> payResult = payService.wechatPay(orderId, merchantId, totalAmount, openId);
            result.putAll(payResult);
        } catch (Exception e) {
            logger.error("用户微信支付失败，用户ID: {}, 订单ID: {}", userId, orderId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/pay/alipay")
    public String alipay(@RequestHeader(value = "Authorization", required = false) String headerToken,
            @RequestParam(value = "token", required = false) String urlToken,
            @RequestParam Long orderId,
            @RequestParam String returnUrl) {
        logger.info("用户支付宝支付，订单ID: {}, 返回URL: {}", orderId, returnUrl);
        try {
            // 验证token并获取用户ID
            Long userId = null;
            String token = headerToken != null ? headerToken : urlToken;
            if (token != null && !token.isEmpty()) {
                // 移除Bearer前缀（如果有）
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
                if (jwtUtil.validateToken(token)) {
                    userId = jwtUtil.getUserIdFromToken(token);
                }
            }

            // 如果没有有效的token，返回错误页面
            if (userId == null) {
                return "<h1>请先登录</h1><p>您需要登录后才能进行支付</p>";
            }

            // 获取订单信息
            Map<String, Object> order = orderService.findById(orderId);
            if (order == null) {
                return "<h1>订单不存在</h1>";
            }

            // 检查订单状态
            if (!"pending".equals(order.get("status"))) {
                return "<h1>订单状态不正确，无法支付</h1>";
            }

            // 检查订单所属用户
            if (!userId.equals(order.get("userId"))) {
                return "<h1>无权操作此订单</h1>";
            }

            // 调用支付宝支付服务
            double totalAmount = (double) order.get("totalAmount");
            Long merchantId = (Long) order.get("merchantId");
            // 修改returnUrl，跳回订单页并添加orderId参数
            String orderPageUrl = "http://localhost:5173/orders";
            String modifiedReturnUrl = orderPageUrl + "?orderId=" + orderId;
            return payService.alipay(orderId, merchantId, totalAmount, modifiedReturnUrl);
        } catch (Exception e) {
            logger.error("用户支付宝支付失败，订单ID: {}", orderId, e);
            return "<h1>支付失败</h1><p>" + e.getMessage() + "</p>";
        }
    }

    @PostMapping("/payment/wechat/callback")
    public Map<String, Object> wechatCallback(@RequestBody String notifyData) {
        logger.info("微信支付回调");
        return payService.handleWechatCallback(notifyData);
    }

    @PostMapping("/payment/alipay/callback")
    public Map<String, Object> alipayCallback(@RequestParam Map<String, String> params) {
        logger.info("支付宝支付回调");
        return payService.handleAlipayCallback(params);
    }
}