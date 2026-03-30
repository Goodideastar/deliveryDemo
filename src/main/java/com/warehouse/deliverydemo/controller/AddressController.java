package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.common.Result;
import com.warehouse.deliverydemo.entity.Address;
import com.warehouse.deliverydemo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping
    public Result<?> list(@RequestAttribute("userId") Long userId) {
        try {
            List<Address> addresses = addressService.findByUserId(userId);
            return Result.success(addresses);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<?> get(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        try {
            Address address = addressService.findById(id);
            if (address != null && address.getUserId().equals(userId)) {
                return Result.success(address);
            } else {
                return Result.notFound("地址不存在");
            }
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping
    public Result<?> add(@RequestBody Address address, @RequestAttribute("userId") Long userId) {
        try {
            address.setUserId(userId);
            addressService.save(address);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping
    public Result<?> update(@RequestBody Address address, @RequestAttribute("userId") Long userId) {
        try {
            Address existingAddress = addressService.findById(address.getId());
            if (existingAddress != null && existingAddress.getUserId().equals(userId)) {
                address.setUserId(userId);
                addressService.save(address);
                return Result.success("更新成功");
            } else {
                return Result.notFound("地址不存在");
            }
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        try {
            Address address = addressService.findById(id);
            if (address != null && address.getUserId().equals(userId)) {
                addressService.delete(id);
                return Result.success("删除成功");
            } else {
                return Result.notFound("地址不存在");
            }
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/default/{id}")
    public Result<?> setDefault(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        try {
            Address address = addressService.findById(id);
            if (address != null && address.getUserId().equals(userId)) {
                addressService.setDefault(id, userId);
                return Result.success("设置成功");
            } else {
                return Result.notFound("地址不存在");
            }
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }
}