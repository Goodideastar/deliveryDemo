package com.warehouse.deliverydemo.service;

import com.warehouse.deliverydemo.entity.Merchant;
import com.warehouse.deliverydemo.mapper.MerchantMapper;
import com.warehouse.deliverydemo.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private JWTUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Merchant register(Merchant merchant) {
        if (merchantMapper.findByUsername(merchant.getUsername()) != null) {
            throw new RuntimeException("用户名已被注册");
        }

        if (merchantMapper.findByPhone(merchant.getPhone()) != null) {
            throw new RuntimeException("手机号已被注册");
        }

        if (merchant.getEmail() != null && merchantMapper.findByEmail(merchant.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));

        if (merchant.getRating() == null) {
            merchant.setRating(0.0);
        }
        if (merchant.getSales() == null) {
            merchant.setSales(0);
        }
        if (merchant.getStatus() == null) {
            merchant.setStatus(1);
        }
        if (merchant.getBusinessStatus() == null) {
            merchant.setBusinessStatus(1);
        }

        merchantMapper.insert(merchant);
        return merchant;
    }

    public String login(String username, String password) {
        Merchant merchant = merchantMapper.findByUsername(username);
        if (merchant == null) {
            throw new RuntimeException("用户不存在");
        }

        if (merchant.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }

        if (!passwordEncoder.matches(password, merchant.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return jwtUtil.generateToken(merchant.getId());
    }

    public Merchant findById(Long id) {
        return merchantMapper.findById(id);
    }

    public Map<String, Object> findAll(int page, int size) {
        int offset = (page - 1) * size;
        List<Merchant> merchants = merchantMapper.findAll(offset, size);
        int total = merchantMapper.countAll();

        Map<String, Object> result = new HashMap<>();
        result.put("list", merchants);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);

        return result;
    }

    public Map<String, Object> search(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        List<Merchant> merchants = merchantMapper.search(keyword, offset, size);
        int total = merchantMapper.countSearch(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("list", merchants);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);

        return result;
    }

    public Merchant findByUsername(String username) {
        return merchantMapper.findByUsername(username);
    }

    public void updateBusinessStatus(Long id, Integer status) {
        merchantMapper.updateBusinessStatus(id, status);
    }

    public void updateSales(Long id, Integer sales) {
        merchantMapper.updateSales(id, sales);
    }

    public void incrementSales(Long id) {
        Merchant merchant = merchantMapper.findById(id);
        if (merchant != null) {
            int newSales = (merchant.getSales() != null ? merchant.getSales() : 0) + 1;
            merchantMapper.updateSales(id, newSales);
        }
    }

    public void update(Merchant merchant) {
        merchantMapper.update(merchant);
    }
}