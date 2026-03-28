package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.entity.Merchant;
import com.warehouse.deliverydemo.service.MerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Merchant merchant) {
        Map<String, Object> result = new HashMap<>();
        try {
            Merchant registeredMerchant = merchantService.register(merchant);
            result.put("code", 200);
            result.put("message", "注册成功");
            result.put("data", registeredMerchant);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> result = new HashMap<>();
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");
            String token = merchantService.login(username, password);
            Merchant merchant = merchantService.findByUsername(username);
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("token", token);
            result.put("merchant", merchant);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping
    public Map<String, Object> list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = merchantService.findAll(page, size);
            result.put("code", 200);
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String keyword, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = merchantService.search(keyword, page, size);
            result.put("code", 200);
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Object merchant = merchantService.findById(id);
            result.put("code", 200);
            result.put("data", merchant);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/status")
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> data,
            @RequestAttribute("userId") Long merchantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从请求体中获取状态值
            String statusStr = (String) data.get("status");
            // 将字符串状态转换为整数
            Integer status = "open".equals(statusStr) ? 1 : 0;
            merchantService.updateBusinessStatus(merchantId, status);
            result.put("code", 200);
            result.put("message", "营业状态更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        logger.info("上传商家logo，文件名: {}", file.getOriginalFilename());
        Map<String, Object> result = new HashMap<>();
        try {
            // 生成文件名和路径
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String uploadDir = "G:/Deliverydemo/deliveryDemo/uploads/merchant/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = uploadDir + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);

            // 保存相对路径到数据库
            String relativePath = "/uploads/merchant/" + fileName;
            logger.info("上传商家logo成功，保存路径: {}", relativePath);
            result.put("code", 200);
            result.put("data", relativePath);
            result.put("message", "图片上传成功");
        } catch (Exception e) {
            logger.error("上传商家logo失败", e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Merchant merchant, @RequestAttribute("userId") Long merchantId) {
        logger.info("更新商家信息，商家ID: {}", merchantId);
        Map<String, Object> result = new HashMap<>();
        try {
            merchant.setId(merchantId);
            merchantService.update(merchant);
            logger.info("更新商家信息成功，商家ID: {}", merchantId);
            result.put("code", 200);
            result.put("message", "更新成功");
        } catch (Exception e) {
            logger.error("更新商家信息失败，商家ID: {}", merchantId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

}