package com.warehouse.deliverydemo.service;

import com.warehouse.deliverydemo.entity.Dish;
import com.warehouse.deliverydemo.entity.DishCategory;
import com.warehouse.deliverydemo.mapper.DishCategoryMapper;
import com.warehouse.deliverydemo.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishCategoryMapper dishCategoryMapper;

    public List<DishCategory> findCategoriesByMerchantId(Long merchantId) {
        return dishCategoryMapper.findByMerchantId(merchantId);
    }

    public List<Dish> findByCategoryId(Long categoryId, Long merchantId) {
        return dishMapper.findByCategoryIdAndMerchantId(categoryId, merchantId);
    }

    public Map<String, Object> search(Long merchantId, String keyword, Double minPrice, Double maxPrice, int page,
            int size) {
        int offset = (page - 1) * size;
        List<Dish> dishes = dishMapper.search(merchantId, keyword, minPrice, maxPrice, offset, size);
        int total = dishMapper.countSearch(merchantId, keyword, minPrice, maxPrice);

        Map<String, Object> result = new HashMap<>();
        result.put("list", dishes);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);

        return result;
    }

    public Dish findById(Long id) {
        return dishMapper.findById(id);
    }

    public Dish create(Dish dish) {
        dishMapper.insert(dish);
        return dish;
    }

    public Dish update(Dish dish) {
        dishMapper.update(dish);
        return dish;
    }

    public void delete(Long id) {
        dishMapper.delete(id);
    }

    public DishCategory createCategory(DishCategory category) {
        dishCategoryMapper.insert(category);
        return category;
    }

    public DishCategory updateCategory(DishCategory category) {
        dishCategoryMapper.update(category);
        return category;
    }

    public void deleteCategory(Long id) {
        dishCategoryMapper.delete(id);
    }

    public void updateStock(Long dishId, Integer quantity) {
        dishMapper.updateStock(dishId, quantity);
    }
}