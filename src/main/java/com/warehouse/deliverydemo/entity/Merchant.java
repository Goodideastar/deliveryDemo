package com.warehouse.deliverydemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Merchant {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String name;
    private String address;
    private String description;
    private String logo;
    private Double rating;
    private Integer sales;
    private Integer status; // 账号状态：1-启用，0-禁用
    private Integer businessStatus; // 营业状态：1-营业中，0-休息中
    // 微信支付配置
    private String wechatAppId;
    private String wechatMchId;
    private String wechatApiKey;
    private String wechatNotifyUrl;
    // 支付宝配置
    private String alipayAppId;
    private String alipayPrivateKey;
    private String alipayPublicKey;
    private String alipayGatewayUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}