package com.warehouse.deliverydemo.service;

import java.util.Map;

public interface PayService {
    /**
     * 微信支付下单
     * 
     * @param orderId     订单ID
     * @param merchantId  商家ID
     * @param totalAmount 总金额
     * @param openId      用户OpenID
     * @return 微信支付参数
     */
    Map<String, Object> wechatPay(Long orderId, Long merchantId, double totalAmount, String openId);

    /**
     * 支付宝支付下单
     * 
     * @param orderId     订单ID
     * @param merchantId  商家ID
     * @param totalAmount 总金额
     * @param returnUrl   回调地址
     * @return 支付宝支付表单
     */
    String alipay(Long orderId, Long merchantId, double totalAmount, String returnUrl);

    /**
     * 处理微信支付回调
     * 
     * @param notifyData 回调数据
     * @return 处理结果
     */
    Map<String, Object> handleWechatCallback(String notifyData);

    /**
     * 处理支付宝支付回调
     * 
     * @param params 回调参数
     * @return 处理结果
     */
    Map<String, Object> handleAlipayCallback(Map<String, String> params);
}