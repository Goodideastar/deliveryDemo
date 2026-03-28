package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Map<String, Object> addToCart(@RequestAttribute("userId") Long userId,
            @RequestBody Map<String, Object> params) {
        Object dishIdObj = params.get("dishId");
        Long dishId = null;
        if (dishIdObj instanceof String) {
            dishId = Long.valueOf((String) dishIdObj);
        } else if (dishIdObj instanceof Number) {
            dishId = ((Number) dishIdObj).longValue();
        }

        Object quantityObj = params.get("quantity");
        Integer quantity = null;
        if (quantityObj instanceof String) {
            quantity = Integer.valueOf((String) quantityObj);
        } else if (quantityObj instanceof Number) {
            quantity = ((Number) quantityObj).intValue();
        }

        Object merchantIdObj = params.get("merchantId");
        Long merchantId = null;
        if (merchantIdObj instanceof String) {
            merchantId = Long.valueOf((String) merchantIdObj);
        } else if (merchantIdObj instanceof Number) {
            merchantId = ((Number) merchantIdObj).longValue();
        }

        logger.info("用户添加商品到购物车，用户ID: {}, 商品ID: {}, 数量: {}, 商家ID: {}", userId, dishId, quantity, merchantId);
        Map<String, Object> result = new HashMap<>();
        try {
            cartService.addToCart(userId, dishId, quantity);
            logger.info("用户添加商品到购物车成功，用户ID: {}, 商品ID: {}", userId, dishId);
            result.put("code", 200);
            result.put("message", "添加成功");
        } catch (Exception e) {
            logger.error("用户添加商品到购物车失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> updateQuantity(@RequestAttribute("userId") Long userId,
            @RequestParam Long dishId,
            @RequestParam Integer quantity) {
        logger.info("用户更新购物车商品数量，用户ID: {}, 商品ID: {}, 新数量: {}", userId, dishId, quantity);
        Map<String, Object> result = new HashMap<>();
        try {
            cartService.updateQuantity(userId, dishId, quantity);
            logger.info("用户更新购物车商品数量成功，用户ID: {}, 商品ID: {}", userId, dishId);
            result.put("code", 200);
            result.put("message", "更新成功");
        } catch (Exception e) {
            logger.error("用户更新购物车商品数量失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> updateQuantityPost(@RequestAttribute("userId") Long userId,
            @RequestBody Map<String, Object> params) {
        Object dishIdObj = params.get("dishId");
        Long dishId = null;
        if (dishIdObj instanceof String) {
            dishId = Long.valueOf((String) dishIdObj);
        } else if (dishIdObj instanceof Number) {
            dishId = ((Number) dishIdObj).longValue();
        }

        Object quantityObj = params.get("quantity");
        Integer quantity = null;
        if (quantityObj instanceof String) {
            quantity = Integer.valueOf((String) quantityObj);
        } else if (quantityObj instanceof Number) {
            quantity = ((Number) quantityObj).intValue();
        }

        logger.info("用户更新购物车商品数量(POST)，用户ID: {}, 商品ID: {}, 新数量: {}", userId, dishId, quantity);
        Map<String, Object> result = new HashMap<>();
        try {
            cartService.updateQuantity(userId, dishId, quantity);
            logger.info("用户更新购物车商品数量(POST)成功，用户ID: {}, 商品ID: {}", userId, dishId);
            result.put("code", 200);
            result.put("message", "更新成功");
        } catch (Exception e) {
            logger.error("用户更新购物车商品数量(POST)失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/remove/{dishId}")
    public Map<String, Object> removeFromCart(@RequestAttribute("userId") Long userId,
            @PathVariable Long dishId) {
        logger.info("用户从购物车移除商品，用户ID: {}, 商品ID: {}", userId, dishId);
        Map<String, Object> result = new HashMap<>();
        try {
            cartService.removeFromCart(userId, dishId);
            logger.info("用户从购物车移除商品成功，用户ID: {}, 商品ID: {}", userId, dishId);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            logger.error("用户从购物车移除商品失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/clear")
    public Map<String, Object> clearCart(@RequestAttribute("userId") Long userId) {
        logger.info("用户清空购物车，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();
        try {
            cartService.clearCart(userId);
            logger.info("用户清空购物车成功，用户ID: {}", userId);
            result.put("code", 200);
            result.put("message", "清空成功");
        } catch (Exception e) {
            logger.error("用户清空购物车失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/clear")
    public Map<String, Object> clearCartPost(@RequestAttribute("userId") Long userId) {
        logger.info("用户清空购物车(POST)，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();
        try {
            cartService.clearCart(userId);
            logger.info("用户清空购物车(POST)成功，用户ID: {}", userId);
            result.put("code", 200);
            result.put("message", "清空成功");
        } catch (Exception e) {
            logger.error("用户清空购物车(POST)失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping
    public Map<String, Object> getCart(@RequestAttribute("userId") Long userId) {
        logger.info("用户查询购物车，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();
        try {
            Object items = cartService.getCartItems(userId);
            double total = cartService.calculateTotal(userId);
            int itemCount = 0;
            if (items instanceof java.util.List) {
                itemCount = ((java.util.List<?>) items).size();
            }
            logger.info("用户查询购物车成功，用户ID: {}, 商品数量: {}", userId, itemCount);
            result.put("code", 200);
            result.put("data", items);
            result.put("total", total);
        } catch (Exception e) {
            logger.error("用户查询购物车失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/by-merchant")
    public Map<String, Object> getCartByMerchant(@RequestAttribute("userId") Long userId) {
        logger.info("用户按商家查询购物车，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();
        try {
            Object itemsByMerchant = cartService.getCartItemsByMerchant(userId);
            logger.info("用户按商家查询购物车成功，用户ID: {}", userId);
            result.put("code", 200);
            result.put("data", itemsByMerchant);
        } catch (Exception e) {
            logger.error("用户按商家查询购物车失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}