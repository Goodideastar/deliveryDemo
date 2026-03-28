package com.warehouse.deliverydemo.service;

import com.warehouse.deliverydemo.entity.User;
import com.warehouse.deliverydemo.mapper.UserMapper;
import com.warehouse.deliverydemo.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JWTUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(User user) {
        // 检查手机号是否已存在
        if (userMapper.findByPhone(user.getPhone()) != null) {
            throw new RuntimeException("手机号已被注册");
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && userMapper.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 设置默认值
        if (user.getRole() == null) {
            user.setRole("user");
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        // 保存用户
        userMapper.insert(user);
        return user;
    }

    public String login(String phone, String password) {
        // 根据手机号或用户名查找用户
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            user = userMapper.findByUsername(phone);
        }
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成Token
        return jwtUtil.generateToken(user.getId());
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public User findByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }
    
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}