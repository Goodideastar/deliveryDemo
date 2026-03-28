package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.entity.Address;
import com.warehouse.deliverydemo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    
    @GetMapping
    public Map<String, Object> list(@RequestAttribute("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Address> addresses = addressService.findByUserId(userId);
            result.put("code", 200);
            result.put("data", addresses);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Address address = addressService.findById(id);
            if (address != null && address.getUserId().equals(userId)) {
                result.put("code", 200);
                result.put("data", address);
            } else {
                result.put("code", 404);
                result.put("message", "地址不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping
    public Map<String, Object> add(@RequestBody Address address, @RequestAttribute("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            address.setUserId(userId);
            addressService.save(address);
            result.put("code", 200);
            result.put("message", "添加成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PutMapping
    public Map<String, Object> update(@RequestBody Address address, @RequestAttribute("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Address existingAddress = addressService.findById(address.getId());
            if (existingAddress != null && existingAddress.getUserId().equals(userId)) {
                address.setUserId(userId);
                addressService.save(address);
                result.put("code", 200);
                result.put("message", "更新成功");
            } else {
                result.put("code", 404);
                result.put("message", "地址不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Address address = addressService.findById(id);
            if (address != null && address.getUserId().equals(userId)) {
                addressService.delete(id);
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "地址不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PutMapping("/default/{id}")
    public Map<String, Object> setDefault(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Address address = addressService.findById(id);
            if (address != null && address.getUserId().equals(userId)) {
                addressService.setDefault(id, userId);
                result.put("code", 200);
                result.put("message", "设置成功");
            } else {
                result.put("code", 404);
                result.put("message", "地址不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}