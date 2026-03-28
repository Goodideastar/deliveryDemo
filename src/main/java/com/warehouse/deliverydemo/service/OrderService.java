package com.warehouse.deliverydemo.service;

import com.warehouse.deliverydemo.entity.Order;
import com.warehouse.deliverydemo.entity.OrderItem;
import com.warehouse.deliverydemo.entity.CartItem;
import com.warehouse.deliverydemo.entity.Address;
import com.warehouse.deliverydemo.entity.Merchant;
import com.warehouse.deliverydemo.mapper.OrderMapper;
import com.warehouse.deliverydemo.mapper.OrderItemMapper;
import com.warehouse.deliverydemo.utils.RegionCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.warehouse.deliverydemo.entity.Dish;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DishService dishService;

    @Autowired
    private MerchantService merchantService;

    @Transactional
    public Order createOrder(Long userId, Long merchantId, Long addressId, String remark) {
        // 获取购物车商品
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        // 过滤出指定商家的商品
        List<CartItem> merchantItems = cartItems.stream()
                .filter(item -> item.getMerchantId().equals(merchantId))
                .toList();
        if (merchantItems.isEmpty()) {
            throw new RuntimeException("购物车中没有该商家的商品");
        }

        // 计算总价
        double totalAmount = merchantItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // 获取收货地址
        Address address = addressService.findById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在");
        }

        // 生成订单号
        String orderNo = generateOrderNo();

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setMerchantId(merchantId);
        order.setAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        // 将地址编码转换为文字
        String shippingAddress = RegionCodeUtil.formatAddress(
                address.getProvince(),
                address.getCity(),
                address.getDistrict(),
                address.getDetail());
        order.setShippingAddress(shippingAddress);
        order.setRemark(remark);
        orderMapper.insert(order);

        // 创建订单明细
        for (CartItem item : merchantItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setDishId(item.getDishId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItemMapper.insert(orderItem);

            // 库存更新将在支付成功后进行
        }

        // 清空购物车中该商家的商品
        for (CartItem item : merchantItems) {
            cartService.removeFromCart(userId, item.getDishId());
        }

        return order;
    }

    public Map<String, Object> findById(Long id) {
        Order order = orderMapper.findById(id);
        if (order != null) {
            // 转换为Map对象
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("userId", order.getUserId());
            orderMap.put("userName", order.getUserName());
            orderMap.put("merchantId", order.getMerchantId());
            orderMap.put("addressId", order.getAddressId());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("status", order.getStatus());
            orderMap.put("paymentMethod", order.getPaymentMethod());
            orderMap.put("paymentTime", order.getPaymentTime());
            orderMap.put("shippingAddress", order.getShippingAddress());
            orderMap.put("remark", order.getRemark());
            orderMap.put("createTime", order.getCreatedAt());
            orderMap.put("updateTime", order.getUpdatedAt());

            // 获取地址信息
            if (order.getAddressId() != null) {
                Address address = addressService.findById(order.getAddressId());
                if (address != null) {
                    orderMap.put("province", address.getProvince());
                    orderMap.put("city", address.getCity());
                    orderMap.put("district", address.getDistrict());
                    orderMap.put("detail", address.getDetail());
                    orderMap.put("phone", address.getPhone());
                }
            }

            // 获取商家信息
            if (order.getMerchantId() != null) {
                try {
                    Merchant merchant = merchantService.findById(order.getMerchantId());
                    if (merchant != null) {
                        orderMap.put("merchantName", merchant.getName());
                    }
                } catch (Exception e) {
                    // 忽略错误，确保即使商家不存在也能返回订单信息
                }
            }

            // 获取订单商品信息
            List<Map<String, Object>> orderItems = findOrderItems(id);
            orderMap.put("items", orderItems);

            return orderMap;
        }
        return null;
    }

    public Order findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    public Map<String, Object> findByUserId(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        List<Order> orders = orderMapper.findByUserId(userId, offset, size);
        int total = orderMapper.countByUserId(userId);

        // 转换订单列表为包含完整信息的Map列表
        List<Map<String, Object>> orderMaps = new ArrayList<>();
        for (Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("userId", order.getUserId());
            orderMap.put("userName", order.getUserName());
            orderMap.put("merchantId", order.getMerchantId());
            orderMap.put("addressId", order.getAddressId());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("status", order.getStatus());
            orderMap.put("paymentMethod", order.getPaymentMethod());
            orderMap.put("paymentTime", order.getPaymentTime());
            orderMap.put("shippingAddress", order.getShippingAddress());
            orderMap.put("remark", order.getRemark());
            orderMap.put("createTime", order.getCreatedAt());
            orderMap.put("updateTime", order.getUpdatedAt());

            // 获取地址信息
            if (order.getAddressId() != null) {
                Address address = addressService.findById(order.getAddressId());
                if (address != null) {
                    orderMap.put("province", address.getProvince());
                    orderMap.put("city", address.getCity());
                    orderMap.put("district", address.getDistrict());
                    orderMap.put("detail", address.getDetail());
                }
            }

            // 获取商家信息
            if (order.getMerchantId() != null) {
                try {
                    Merchant merchant = merchantService.findById(order.getMerchantId());
                    if (merchant != null) {
                        orderMap.put("merchantName", merchant.getName());
                    }
                } catch (Exception e) {
                    // 忽略错误，确保即使商家不存在也能返回订单信息
                }
            }

            // 获取订单商品信息
            List<Map<String, Object>> orderItems = findOrderItems(order.getId());
            orderMap.put("items", orderItems);

            orderMaps.add(orderMap);
        }

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("list", orderMaps);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);

        return result;
    }

    public Map<String, Object> findByMerchantId(Long merchantId, int page, int size) {
        int offset = (page - 1) * size;
        List<Order> orders = orderMapper.findByMerchantId(merchantId, offset, size);
        int total = orderMapper.countByMerchantId(merchantId);

        // 转换订单列表为包含完整信息的Map列表
        List<Map<String, Object>> orderMaps = new ArrayList<>();
        for (Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("userId", order.getUserId());
            orderMap.put("userName", order.getUserName());
            orderMap.put("merchantId", order.getMerchantId());
            orderMap.put("addressId", order.getAddressId());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("status", order.getStatus());
            orderMap.put("paymentMethod", order.getPaymentMethod());
            orderMap.put("paymentTime", order.getPaymentTime());
            orderMap.put("shippingAddress", order.getShippingAddress());
            orderMap.put("remark", order.getRemark());
            orderMap.put("createTime", order.getCreatedAt());
            orderMap.put("updateTime", order.getUpdatedAt());

            // 获取地址信息
            if (order.getAddressId() != null) {
                Address address = addressService.findById(order.getAddressId());
                if (address != null) {
                    orderMap.put("province", address.getProvince());
                    orderMap.put("city", address.getCity());
                    orderMap.put("district", address.getDistrict());
                    orderMap.put("detail", address.getDetail());
                }
            }

            // 获取商家信息
            if (order.getMerchantId() != null) {
                try {
                    Merchant merchant = merchantService.findById(order.getMerchantId());
                    if (merchant != null) {
                        orderMap.put("merchantName", merchant.getName());
                    }
                } catch (Exception e) {
                    // 忽略错误，确保即使商家不存在也能返回订单信息
                }
            }

            // 获取订单商品信息
            List<Map<String, Object>> orderItems = findOrderItems(order.getId());
            orderMap.put("items", orderItems);

            orderMaps.add(orderMap);
        }

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("list", orderMaps);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);

        return result;
    }

    public List<Map<String, Object>> findOrderItems(Long orderId) {
        List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (OrderItem item : orderItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("orderId", item.getOrderId());
            itemMap.put("dishId", item.getDishId());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("price", item.getPrice());
            itemMap.put("createdAt", item.getCreatedAt());
            itemMap.put("updatedAt", item.getUpdatedAt());

            // 添加菜品名称和图片信息
            try {
                Dish dish = dishService.findById(item.getDishId());
                if (dish != null) {
                    itemMap.put("dishName", dish.getName());
                    itemMap.put("image", dish.getImage());
                }
            } catch (Exception e) {
                // 忽略错误，确保即使菜品不存在也能返回订单商品信息
            }

            result.add(itemMap);
        }

        return result;
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status, String paymentMethod) {
        Order order = orderMapper.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            if ("paid".equals(status)) {
                order.setPaymentTime(LocalDateTime.now());
                if (paymentMethod != null) {
                    order.setPaymentMethod(paymentMethod);
                }
                // 增加商家月售数量
                merchantService.incrementSales(order.getMerchantId());
                // 扣减菜品库存
                List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
                for (OrderItem item : orderItems) {
                    dishService.updateStock(item.getDishId(), item.getQuantity());
                }
            }
            orderMapper.update(order);
        }
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        updateOrderStatus(orderId, status, null);
    }

    @Transactional
    public void handlePaymentCallback(String orderNo, String paymentMethod) {
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order != null && "pending".equals(order.getStatus())) {
            order.setStatus("paid");
            order.setPaymentMethod(paymentMethod);
            order.setPaymentTime(LocalDateTime.now());
            orderMapper.update(order);
            // 增加商家月售数量
            merchantService.incrementSales(order.getMerchantId());
            // 扣减菜品库存
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());
            for (OrderItem item : orderItems) {
                dishService.updateStock(item.getDishId(), item.getQuantity());
            }
        }
    }

    private String generateOrderNo() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "ORD" + System.currentTimeMillis() + uuid.substring(0, 8);
    }
}
