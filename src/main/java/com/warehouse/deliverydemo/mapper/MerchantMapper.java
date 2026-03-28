package com.warehouse.deliverydemo.mapper;

import com.warehouse.deliverydemo.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MerchantMapper {
    List<Merchant> findAll(@Param("offset") int offset, @Param("limit") int limit);

    int countAll();

    Merchant findById(@Param("id") Long id);

    List<Merchant> search(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    int countSearch(@Param("keyword") String keyword);

    Merchant findByUsername(@Param("username") String username);

    Merchant findByPhone(@Param("phone") String phone);

    Merchant findByEmail(@Param("email") String email);

    void insert(Merchant merchant);

    void updateBusinessStatus(@Param("id") Long id, @Param("status") Integer status);

    void updateSales(@Param("id") Long id, @Param("sales") Integer sales);

    void update(Merchant merchant);
}