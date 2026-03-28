package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.entity.User;
import com.warehouse.deliverydemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> registerData) {
        String username = (String) registerData.get("username");
        String phone = (String) registerData.get("phone");
        String role = (String) registerData.get("role");
        logger.info("用户注册，用户名: {}, 手机号: {}, 角色: {}", username, phone, role);
        Map<String, Object> result = new HashMap<>();
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword((String) registerData.get("password"));
            user.setPhone(phone);
            user.setEmail((String) registerData.get("email"));
            user.setRole(role);

            User registeredUser = userService.register(user);
            logger.info("用户注册成功，用户ID: {}, 用户名: {}", registeredUser.getId(), registeredUser.getUsername());
            result.put("code", 200);
            result.put("message", "注册成功");
            result.put("data", registeredUser);
        } catch (Exception e) {
            logger.error("用户注册失败，用户名: {}, 手机号: {}", username, phone, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String phone = loginData.get("username");
        logger.info("用户登录，手机号/用户名: {}", phone);
        Map<String, Object> result = new HashMap<>();
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
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("token", token);
            result.put("user", user);
        } catch (Exception e) {
            logger.error("用户登录失败，手机号/用户名: {}", phone, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo(@RequestAttribute("userId") Long userId) {
        logger.info("获取用户信息，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.findById(userId);
            logger.info("获取用户信息成功，用户ID: {}, 用户名: {}", userId, user.getUsername());
            result.put("code", 200);
            result.put("data", user);
        } catch (Exception e) {
            logger.error("获取用户信息失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestAttribute("userId") Long userId, @RequestBody User user) {
        logger.info("更新用户信息，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();
        try {
            user.setId(userId);
            userService.update(user);
            logger.info("更新用户信息成功，用户ID: {}", userId);
            result.put("code", 200);
            result.put("message", "更新成功");
        } catch (Exception e) {
            logger.error("更新用户信息失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/upload/avatar")
    public Map<String, Object> uploadAvatar(@RequestAttribute("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        logger.info("上传用户头像，用户ID: {}, 文件名: {}", userId, file.getOriginalFilename());
        Map<String, Object> result = new HashMap<>();
        try {
            if (file.isEmpty()) {
                logger.warn("上传用户头像失败，文件为空，用户ID: {}", userId);
                result.put("code", 400);
                result.put("message", "文件为空");
                return result;
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
            result.put("code", 200);
            result.put("message", "上传成功");
            result.put("data", relativePath);
        } catch (Exception e) {
            logger.error("上传用户头像失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}