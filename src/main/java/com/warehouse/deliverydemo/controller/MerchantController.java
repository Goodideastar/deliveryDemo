package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.common.Result;
import com.warehouse.deliverydemo.entity.Merchant;
import com.warehouse.deliverydemo.service.MerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.util.Map;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody Merchant merchant) {
        try {
            Merchant registeredMerchant = merchantService.register(merchant);
            return Result.success("注册成功", registeredMerchant);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");
            String token = merchantService.login(username, password);
            Merchant merchant = merchantService.findByUsername(username);
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("token", token);
            data.put("merchant", merchant);
            return Result.success("登录成功", data);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> data = merchantService.findAll(page, size);
            return Result.success(data);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/search")
    public Result<?> search(@RequestParam String keyword, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> data = merchantService.search(keyword, page, size);
            return Result.success(data);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<?> get(@PathVariable Long id) {
        try {
            Object merchant = merchantService.findById(id);
            return Result.success(merchant);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/status")
    public Result<?> updateStatus(@RequestBody Map<String, Object> data,
            @RequestAttribute("userId") Long merchantId) {
        try {
            // 从请求体中获取状态值
            String statusStr = (String) data.get("status");
            // 将字符串状态转换为整数
            Integer status = "open".equals(statusStr) ? 1 : 0;
            merchantService.updateBusinessStatus(merchantId, status);
            return Result.success("营业状态更新成功");
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        logger.info("上传商家logo，文件名: {}", file.getOriginalFilename());
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
            return Result.success("图片上传成功", relativePath);
        } catch (Exception e) {
            logger.error("上传商家logo失败", e);
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Merchant merchant, @RequestAttribute("userId") Long merchantId) {
        logger.info("更新商家信息，商家ID: {}", merchantId);
        try {
            merchant.setId(merchantId);
            merchantService.update(merchant);
            logger.info("更新商家信息成功，商家ID: {}", merchantId);
            return Result.success("更新成功");
        } catch (Exception e) {
            logger.error("更新商家信息失败，商家ID: {}", merchantId, e);
            return Result.serverError(e.getMessage());
        }
    }

}