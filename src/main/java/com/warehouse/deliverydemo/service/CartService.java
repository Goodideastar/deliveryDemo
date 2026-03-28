package com.warehouse.deliverydemo.service;

import com.warehouse.deliverydemo.entity.CartItem;
import com.warehouse.deliverydemo.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DishService dishService;

    private static final String CART_PREFIX = "cart:";

    public void addToCart(Long userId, Long dishId, Integer quantity) {
        String cartKey = CART_PREFIX + userId;

        // 获取菜品信息
        Dish dish = dishService.findById(dishId);
        if (dish == null) {
            throw new RuntimeException("菜品不存在");
        }

        // 从Redis获取购物车
        Map<Long, CartItem> cart = getCart(userId);

        // 检查是否已存在该菜品（处理可能的类型转换问题）
        boolean containsKey = cart.containsKey(dishId);
        Long foundKey = dishId;

        if (!containsKey) {
            // 尝试处理Redis反序列化后键可能为String的情况
            try {
                // 创建一个新的Map来存储转换后的键值对
                Map<Long, CartItem> newCart = new HashMap<>();
                for (Object key : cart.keySet()) {
                    Long longKey;
                    if (key instanceof String) {
                        longKey = Long.valueOf((String) key);
                    } else if (key instanceof Number) {
                        longKey = ((Number) key).longValue();
                    } else {
                        continue;
                    }
                    newCart.put(longKey, cart.get(key));

                    // 检查是否匹配当前的dishId
                    if (longKey.equals(dishId)) {
                        foundKey = longKey;
                        containsKey = true;
                    }
                }

                // 如果发现了String类型的键，使用新的Map
                if (!newCart.isEmpty()) {
                    cart = newCart;
                }
            } catch (Exception e) {
                // 忽略转换错误
            }
        }

        if (containsKey) {
            CartItem item = cart.get(foundKey);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setDishId(dishId);
            item.setDishName(dish.getName());
            item.setPrice(dish.getPrice());
            item.setQuantity(quantity);
            item.setImage(dish.getImage());
            item.setMerchantId(dish.getMerchantId());
            cart.put(dishId, item);
        }

        // 保存回Redis
        redisTemplate.opsForValue().set(cartKey, cart);
    }

    public void updateQuantity(Long userId, Long dishId, Integer quantity) {
        String cartKey = CART_PREFIX + userId;
        Map<Long, CartItem> cart = getCart(userId);

        // 检查是否存在该商品（处理可能的类型转换问题）
        boolean containsKey = cart.containsKey(dishId);
        Long foundKey = dishId;

        if (!containsKey) {
            // 尝试处理Redis反序列化后键可能为String的情况
            try {
                // 创建一个新的Map来存储转换后的键值对
                Map<Long, CartItem> newCart = new HashMap<>();
                for (Object key : cart.keySet()) {
                    Long longKey;
                    if (key instanceof String) {
                        longKey = Long.valueOf((String) key);
                    } else if (key instanceof Number) {
                        longKey = ((Number) key).longValue();
                    } else {
                        continue;
                    }
                    newCart.put(longKey, cart.get(key));

                    // 检查是否匹配当前的dishId
                    if (longKey.equals(dishId)) {
                        foundKey = longKey;
                        containsKey = true;
                    }
                }

                // 如果发现了String类型的键，使用新的Map
                if (!newCart.isEmpty()) {
                    cart = newCart;
                }
            } catch (Exception e) {
                // 忽略转换错误
            }
        }

        if (containsKey) {
            if (quantity <= 0) {
                cart.remove(foundKey);
            } else {
                CartItem item = cart.get(foundKey);
                item.setQuantity(quantity);
            }
            redisTemplate.opsForValue().set(cartKey, cart);
        }
    }

    public void removeFromCart(Long userId, Long dishId) {
        String cartKey = CART_PREFIX + userId;
        Map<Long, CartItem> cart = getCart(userId);

        // 检查是否存在该商品（处理可能的类型转换问题）
        boolean containsKey = cart.containsKey(dishId);
        Long foundKey = dishId;

        if (!containsKey) {
            // 尝试处理Redis反序列化后键可能为String的情况
            try {
                // 创建一个新的Map来存储转换后的键值对
                Map<Long, CartItem> newCart = new HashMap<>();
                for (Object key : cart.keySet()) {
                    Long longKey;
                    if (key instanceof String) {
                        longKey = Long.valueOf((String) key);
                    } else if (key instanceof Number) {
                        longKey = ((Number) key).longValue();
                    } else {
                        continue;
                    }
                    newCart.put(longKey, cart.get(key));

                    // 检查是否匹配当前的dishId
                    if (longKey.equals(dishId)) {
                        foundKey = longKey;
                        containsKey = true;
                    }
                }

                // 如果发现了String类型的键，使用新的Map
                if (!newCart.isEmpty()) {
                    cart = newCart;
                }
            } catch (Exception e) {
                // 忽略转换错误
            }
        }

        if (containsKey) {
            cart.remove(foundKey);
            redisTemplate.opsForValue().set(cartKey, cart);
        }
    }

    public void clearCart(Long userId) {
        String cartKey = CART_PREFIX + userId;
        redisTemplate.delete(cartKey);
    }

    public List<CartItem> getCartItems(Long userId) {
        Map<Long, CartItem> cart = getCart(userId);
        return new ArrayList<>(cart.values());
    }

    public Map<Long, List<CartItem>> getCartItemsByMerchant(Long userId) {
        List<CartItem> items = getCartItems(userId);
        Map<Long, List<CartItem>> result = new HashMap<>();

        for (CartItem item : items) {
            Long merchantId = item.getMerchantId();
            if (!result.containsKey(merchantId)) {
                result.put(merchantId, new ArrayList<>());
            }
            result.get(merchantId).add(item);
        }

        return result;
    }

    public double calculateTotal(Long userId) {
        List<CartItem> items = getCartItems(userId);
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    private Map<Long, CartItem> getCart(Long userId) {
        String cartKey = CART_PREFIX + userId;
        Map<Long, CartItem> cart = (Map<Long, CartItem>) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            cart = new HashMap<>();
        }
        return cart;
    }
}