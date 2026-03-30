package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.common.Result;
import com.warehouse.deliverydemo.entity.User;
import com.warehouse.deliverydemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<String, Object> registerData) {
        String username = (String) registerData.get("username");
        String phone = (String) registerData.get("phone");
        String role = (String) registerData.get("role");
        logger.info("用户注册，用户名: {}, 手机号: {}, 角色: {}", username, phone, role);
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword((String) registerData.get("password"));
            user.setPhone(phone);
            user.setEmail((String) registerData.get("email"));
            user.setRole(role);

            User registeredUser = userService.register(user);
            logger.info("用户注册成功，用户ID: {}, 用户名: {}", registeredUser.getId(), registeredUser.getUsername());
            return Result.success("注册成功", registeredUser);
        } catch (Exception e) {
            logger.error("用户注册失败，用户名: {}, 手机号: {}", username, phone, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> loginData) {
        String phone = loginData.get("username");
        logger.info("用户登录，手机号/用户名: {}", phone);
        try {
            String password = loginData.get("password");
            String token = userService.login(phone, password);
            // 先尝试通过手机号查找用户
            User user = userService.findByPhone(phone);
            if (user == null) {
                // 如果找不到，尝试通过用户名查找
                user = userService.findByUsername(phone);
            }
            logger.info("用户登录成功，用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("token", token);
            data.put("user", user);
            return Result.success("登录成功", data);
        } catch (Exception e) {
            logger.error("用户登录失败，手机号/用户名: {}", phone, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<?> getInfo(@RequestAttribute("userId") Long userId) {
        logger.info("获取用户信息，用户ID: {}", userId);
        try {
            User user = userService.findById(userId);
            logger.info("获取用户信息成功，用户ID: {}, 用户名: {}", userId, user.getUsername());
            return Result.success(user);
        } catch (Exception e) {
            logger.error("获取用户信息失败，用户ID: {}", userId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestAttribute("userId") Long userId, @RequestBody User user) {
        logger.info("更新用户信息，用户ID: {}", userId);
        try {
            user.setId(userId);
            userService.update(user);
            logger.info("更新用户信息成功，用户ID: {}", userId);
            return Result.success("更新成功");
        } catch (Exception e) {
            logger.error("更新用户信息失败，用户ID: {}", userId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/upload/avatar")
    public Result<?> uploadAvatar(@RequestAttribute("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        logger.info("上传用户头像，用户ID: {}, 文件名: {}", userId, file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                logger.warn("上传用户头像失败，文件为空，用户ID: {}", userId);
                return Result.badRequest("文件为空");
            }

            // 生成文件名
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            // 保存文件到本地
            String uploadDir = "G:/Deliverydemo/deliveryDemo/uploads/avatar/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = uploadDir + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);

            // 保存相对路径到数据库
            String relativePath = "/uploads/avatar/" + fileName;

            // 更新用户头像
            User user = userService.findById(userId);
            user.setAvatar(relativePath);
            userService.update(user);

            logger.info("上传用户头像成功，用户ID: {}, 保存路径: {}", userId, relativePath);
            return Result.success("上传成功", relativePath);
        } catch (Exception e) {
            logger.error("上传用户头像失败，用户ID: {}", userId, e);
            return Result.serverError(e.getMessage());
        }
    }
}