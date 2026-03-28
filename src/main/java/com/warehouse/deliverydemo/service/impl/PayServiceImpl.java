package com.warehouse.deliverydemo.service.impl;

import com.warehouse.deliverydemo.service.PayService;
import com.warehouse.deliverydemo.service.OrderService;
import com.warehouse.deliverydemo.service.MerchantService;
import com.warehouse.deliverydemo.entity.Merchant;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {
    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private MerchantService merchantService;

    // 默认微信支付配置（当商家没有配置时使用）
    @Value("${wechat.app-id}")
    private String defaultWechatAppId;

    @Value("${wechat.mch-id}")
    private String defaultWechatMchId;

    @Value("${wechat.api-key}")
    private String defaultWechatApiKey;

    @Value("${wechat.notify-url}")
    private String defaultWechatNotifyUrl;

    @Value("${wechat.trade-type}")
    private String defaultWechatTradeType;

    // 默认支付宝配置（当商家没有配置时使用）
    @Value("${alipay.app-id}")
    private String defaultAlipayAppId;

    @Value("${alipay.private-key}")
    private String defaultAlipayPrivateKey;

    @Value("${alipay.public-key}")
    private String defaultAlipayPublicKey;

    @Value("${alipay.gateway-url}")
    private String defaultAlipayGatewayUrl;

    @Value("${alipay.notify-url}")
    private String defaultAlipayNotifyUrl;

    @Override
    public Map<String, Object> wechatPay(Long orderId, Long merchantId, double totalAmount, String openId) {
        logger.info("微信支付下单，订单ID: {}, 商家ID: {}, 总金额: {}, OpenID: {}", orderId, merchantId, totalAmount, openId);
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取商家支付配置
            Merchant merchant = merchantService.findById(merchantId);
            String wechatAppId = (merchant != null && merchant.getWechatAppId() != null) ? merchant.getWechatAppId()
                    : defaultWechatAppId;
            String wechatMchId = (merchant != null && merchant.getWechatMchId() != null) ? merchant.getWechatMchId()
                    : defaultWechatMchId;
            String wechatApiKey = (merchant != null && merchant.getWechatApiKey() != null) ? merchant.getWechatApiKey()
                    : defaultWechatApiKey;
            String wechatNotifyUrl = (merchant != null && merchant.getWechatNotifyUrl() != null)
                    ? merchant.getWechatNotifyUrl()
                    : defaultWechatNotifyUrl;

            // 模拟微信支付下单
            // 实际项目中应该调用微信支付SDK
            result.put("code", 200);
            result.put("message", "微信支付下单成功");
            result.put("payParams", new HashMap<String, Object>() {
                {
                    put("appId", wechatAppId);
                    put("timeStamp", System.currentTimeMillis() / 1000);
                    put("nonceStr", "test-nonce-str");
                    put("package", "prepay_id=test-prepay-id");
                    put("signType", "MD5");
                    put("paySign", "test-pay-sign");
                }
            });
        } catch (Exception e) {
            logger.error("微信支付下单失败，订单ID: {}, 商家ID: {}", orderId, merchantId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public String alipay(Long orderId, Long merchantId, double totalAmount, String returnUrl) {
        logger.info("支付宝支付下单，订单ID: {}, 商家ID: {}, 总金额: {}", orderId, merchantId, totalAmount);
        try {
            // 获取商家支付配置
            Merchant merchant = merchantService.findById(merchantId);
            String alipayAppId = (merchant != null && merchant.getAlipayAppId() != null) ? merchant.getAlipayAppId()
                    : defaultAlipayAppId;
            String alipayPrivateKey = (merchant != null && merchant.getAlipayPrivateKey() != null)
                    ? merchant.getAlipayPrivateKey()
                    : defaultAlipayPrivateKey;
            String alipayPublicKey = (merchant != null && merchant.getAlipayPublicKey() != null)
                    ? merchant.getAlipayPublicKey()
                    : defaultAlipayPublicKey;
            String alipayGatewayUrl = (merchant != null && merchant.getAlipayGatewayUrl() != null)
                    ? merchant.getAlipayGatewayUrl()
                    : defaultAlipayGatewayUrl;

            // 初始化支付宝客户端
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayGatewayUrl,
                    alipayAppId,
                    alipayPrivateKey,
                    "JSON",
                    "UTF-8",
                    alipayPublicKey,
                    "RSA2");

            // 创建支付宝支付请求
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(returnUrl);
            request.setNotifyUrl(defaultAlipayNotifyUrl); // 回调地址

            // 构建业务参数
            String bizContent = "{" +
                    "\"out_trade_no\":\"ORDER_" + orderId + "\"," +
                    "\"total_amount\":\"" + totalAmount + "\"," +
                    "\"subject\":\"订单支付\"," +
                    "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                    "}";

            request.setBizContent(bizContent);

            // 发起支付请求
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {
                logger.info("支付宝支付下单成功，订单ID: {}", orderId);
                return response.getBody();
            } else {
                logger.error("支付宝支付下单失败: {}", response.getMsg());
                return "<h1>支付失败</h1><p>" + response.getMsg() + "</p>";
            }
        } catch (Exception e) {
            logger.error("支付宝支付下单失败，订单ID: {}, 商家ID: {}", orderId, merchantId, e);
            return "<h1>支付失败</h1><p>" + e.getMessage() + "</p>";
        }
    }

    @Override
    public Map<String, Object> handleWechatCallback(String notifyData) {
        logger.info("处理微信支付回调");
        Map<String, Object> result = new HashMap<>();
        try {
            // 模拟处理微信支付回调
            // 实际项目中应该解析回调数据并验证签名
            result.put("code", 200);
            result.put("message", "回调处理成功");
            result.put("return_code", "SUCCESS");
            result.put("return_msg", "OK");
        } catch (Exception e) {
            logger.error("处理微信支付回调失败", e);
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("return_code", "FAIL");
            result.put("return_msg", e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> handleAlipayCallback(Map<String, String> params) {
        logger.info("处理支付宝支付回调");
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取订单号
            String orderNo = params.get("out_trade_no");
            if (orderNo == null || !orderNo.startsWith("ORDER_")) {
                logger.warn("无效的订单号: {}", orderNo);
                result.put("code", 400);
                result.put("message", "无效的订单号");
                result.put("alipay_result", "fail");
                return result;
            }

            // 解析订单ID
            Long orderId = Long.parseLong(orderNo.replace("ORDER_", ""));

            // 获取订单信息
            Map<String, Object> order = orderService.findById(orderId);
            if (order == null) {
                logger.warn("订单不存在: {}", orderId);
                result.put("code", 404);
                result.put("message", "订单不存在");
                result.put("alipay_result", "fail");
                return result;
            }

            // 获取商家ID
            Long merchantId = (Long) order.get("merchantId");

            // 获取商家支付配置
            Merchant merchant = merchantService.findById(merchantId);
            String alipayPublicKey = (merchant != null && merchant.getAlipayPublicKey() != null)
                    ? merchant.getAlipayPublicKey()
                    : defaultAlipayPublicKey;

            // 验证支付宝签名
            boolean verifyResult = AlipaySignature.rsaCheckV1(
                    params,
                    alipayPublicKey,
                    "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // 验证成功，处理订单
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    // 更新订单状态
                    orderService.updateOrderStatus(orderId, "paid", "alipay");
                    logger.info("支付宝支付回调处理成功，订单ID: {}", orderId);
                    result.put("code", 200);
                    result.put("message", "回调处理成功");
                    result.put("alipay_result", "success");
                } else {
                    logger.warn("支付宝支付状态不正确: {}", tradeStatus);
                    result.put("code", 400);
                    result.put("message", "支付状态不正确");
                    result.put("alipay_result", "fail");
                }
            } else {
                logger.warn("支付宝签名验证失败");
                result.put("code", 400);
                result.put("message", "签名验证失败");
                result.put("alipay_result", "fail");
            }
        } catch (Exception e) {
            logger.error("处理支付宝支付回调失败", e);
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("alipay_result", "fail");
        }
        return result;
    }
}