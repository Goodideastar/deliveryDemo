package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.common.Result;
import com.warehouse.deliverydemo.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Result<?> addToCart(@RequestAttribute("userId") Long userId,
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
        try {
            cartService.addToCart(userId, dishId, quantity);
            logger.info("用户添加商品到购物车成功，用户ID: {}, 商品ID: {}", userId, dishId);
            return Result.success("添加成功");
        } catch (Exception e) {
            logger.error("用户添加商品到购物车失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> updateQuantity(@RequestAttribute("userId") Long userId,
            @RequestParam Long dishId,
            @RequestParam Integer quantity) {
        logger.info("用户更新购物车商品数量，用户ID: {}, 商品ID: {}, 新数量: {}", userId, dishId, quantity);
        try {
            cartService.updateQuantity(userId, dishId, quantity);
            logger.info("用户更新购物车商品数量成功，用户ID: {}, 商品ID: {}", userId, dishId);
            return Result.success("更新成功");
        } catch (Exception e) {
            logger.error("用户更新购物车商品数量失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/update")
    public Result<?> updateQuantityPost(@RequestAttribute("userId") Long userId,
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
        try {
            cartService.updateQuantity(userId, dishId, quantity);
            logger.info("用户更新购物车商品数量(POST)成功，用户ID: {}, 商品ID: {}", userId, dishId);
            return Result.success("更新成功");
        } catch (Exception e) {
            logger.error("用户更新购物车商品数量(POST)失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{dishId}")
    public Result<?> removeFromCart(@RequestAttribute("userId") Long userId,
            @PathVariable Long dishId) {
        logger.info("用户从购物车移除商品，用户ID: {}, 商品ID: {}", userId, dishId);
        try {
            cartService.removeFromCart(userId, dishId);
            logger.info("用户从购物车移除商品成功，用户ID: {}, 商品ID: {}", userId, dishId);
            return Result.success("删除成功");
        } catch (Exception e) {
            logger.error("用户从购物车移除商品失败，用户ID: {}, 商品ID: {}", userId, dishId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public Result<?> clearCart(@RequestAttribute("userId") Long userId) {
        logger.info("用户清空购物车，用户ID: {}", userId);
        try {
            cartService.clearCart(userId);
            logger.info("用户清空购物车成功，用户ID: {}", userId);
            return Result.success("清空成功");
        } catch (Exception e) {
            logger.error("用户清空购物车失败，用户ID: {}", userId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/clear")
    public Result<?> clearCartPost(@RequestAttribute("userId") Long userId) {
        logger.info("用户清空购物车(POST)，用户ID: {}", userId);
        try {
            cartService.clearCart(userId);
            logger.info("用户清空购物车(POST)成功，用户ID: {}", userId);
            return Result.success("清空成功");
        } catch (Exception e) {
            logger.error("用户清空购物车(POST)失败，用户ID: {}", userId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping
    public Result<?> getCart(@RequestAttribute("userId") Long userId) {
        logger.info("用户查询购物车，用户ID: {}", userId);
        try {
            Object items = cartService.getCartItems(userId);
            double total = cartService.calculateTotal(userId);
            int itemCount = 0;
            if (items instanceof java.util.List) {
                itemCount = ((java.util.List<?>) items).size();
            }
            logger.info("用户查询购物车成功，用户ID: {}, 商品数量: {}", userId, itemCount);
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("items", items);
            data.put("total", total);
            return Result.success(data);
        } catch (Exception e) {
            logger.error("用户查询购物车失败，用户ID: {}", userId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/by-merchant")
    public Result<?> getCartByMerchant(@RequestAttribute("userId") Long userId) {
        logger.info("用户按商家查询购物车，用户ID: {}", userId);
        try {
            Object itemsByMerchant = cartService.getCartItemsByMerchant(userId);
            logger.info("用户按商家查询购物车成功，用户ID: {}", userId);
            return Result.success(itemsByMerchant);
        } catch (Exception e) {
            logger.error("用户按商家查询购物车失败，用户ID: {}", userId, e);
            return Result.serverError(e.getMessage());
        }
    }
}