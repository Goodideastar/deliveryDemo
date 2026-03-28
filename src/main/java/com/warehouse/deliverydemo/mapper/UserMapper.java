package com.warehouse.deliverydemo.mapper;

import com.warehouse.deliverydemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByPhone(@Param("phone") String phone);
    User findByEmail(@Param("email") String email);
    User findByUsername(@Param("username") String username);
    void insert(User user);
    User findById(@Param("id") Long id);
    void update(User user);
}