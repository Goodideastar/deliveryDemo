package com.warehouse.deliverydemo.mapper;

import com.warehouse.deliverydemo.entity.DishCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishCategoryMapper {
    List<DishCategory> findByMerchantId(@Param("merchantId") Long merchantId);

    DishCategory findById(@Param("id") Long id);

    void insert(DishCategory category);

    void update(DishCategory category);

    void delete(@Param("id") Long id);
}