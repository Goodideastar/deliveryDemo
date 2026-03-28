package com.warehouse.deliverydemo.mapper;

import com.warehouse.deliverydemo.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper {
    List<Dish> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Dish> findByCategoryIdAndMerchantId(@Param("categoryId") Long categoryId,
            @Param("merchantId") Long merchantId);

    List<Dish> findByMerchantId(@Param("merchantId") Long merchantId);

    List<Dish> search(@Param("merchantId") Long merchantId, @Param("keyword") String keyword,
            @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("offset") int offset,
            @Param("limit") int limit);

    int countSearch(@Param("merchantId") Long merchantId, @Param("keyword") String keyword,
            @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    Dish findById(@Param("id") Long id);

    void insert(Dish dish);

    void update(Dish dish);

    void delete(@Param("id") Long id);

    void updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}