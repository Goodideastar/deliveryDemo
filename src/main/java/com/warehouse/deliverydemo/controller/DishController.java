package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.entity.Dish;
import com.warehouse.deliverydemo.entity.DishCategory;
import com.warehouse.deliverydemo.service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    private static final Logger logger = LoggerFactory.getLogger(DishController.class);

    @Autowired
    private DishService dishService;

    @GetMapping("/categories/{merchantId}")
    public Map<String, Object> getCategories(@PathVariable Long merchantId) {
        logger.info("查询商家菜品分类，商家ID: {}", merchantId);
        Map<String, Object> result = new HashMap<>();
        try {
            Object categories = dishService.findCategoriesByMerchantId(merchantId);
            logger.info("查询商家菜品分类成功，商家ID: {}", merchantId);
            result.put("code", 200);
            result.put("data", categories);
        } catch (Exception e) {
            logger.error("查询商家菜品分类失败，商家ID: {}", merchantId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/category")
    public Map<String, Object> createCategory(@RequestBody DishCategory category) {
        logger.info("创建菜品分类，商家ID: {}, 分类名称: {}", category.getMerchantId(), category.getName());
        Map<String, Object> result = new HashMap<>();
        try {
            DishCategory createdCategory = dishService.createCategory(category);
            logger.info("创建菜品分类成功，分类ID: {}", createdCategory.getId());
            result.put("code", 200);
            result.put("data", createdCategory);
            result.put("message", "分类创建成功");
        } catch (Exception e) {
            logger.error("创建菜品分类失败", e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/category/{id}")
    public Map<String, Object> updateCategory(@PathVariable Long id, @RequestBody DishCategory category) {
        logger.info("更新菜品分类，分类ID: {}, 分类名称: {}", id, category.getName());
        Map<String, Object> result = new HashMap<>();
        try {
            category.setId(id);
            DishCategory updatedCategory = dishService.updateCategory(category);
            logger.info("更新菜品分类成功，分类ID: {}", id);
            result.put("code", 200);
            result.put("data", updatedCategory);
            result.put("message", "分类更新成功");
        } catch (Exception e) {
            logger.error("更新菜品分类失败，分类ID: {}", id, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/category/{id}")
    public Map<String, Object> deleteCategory(@PathVariable Long id) {
        logger.info("删除菜品分类，分类ID: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            dishService.deleteCategory(id);
            logger.info("删除菜品分类成功，分类ID: {}", id);
            result.put("code", 200);
            result.put("message", "分类删除成功");
        } catch (Exception e) {
            logger.error("删除菜品分类失败，分类ID: {}", id, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/category/{categoryId}")
    public Map<String, Object> getByCategory(@PathVariable Long categoryId, @RequestParam Long merchantId) {
        logger.info("查询分类菜品，分类ID: {}, 商家ID: {}", categoryId, merchantId);
        Map<String, Object> result = new HashMap<>();
        try {
            Object dishes = dishService.findByCategoryId(categoryId, merchantId);
            logger.info("查询分类菜品成功，分类ID: {}, 商家ID: {}", categoryId, merchantId);
            result.put("code", 200);
            result.put("data", dishes);
        } catch (Exception e) {
            logger.error("查询分类菜品失败，分类ID: {}, 商家ID: {}", categoryId, merchantId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/search/{merchantId}")
    public Map<String, Object> search(@PathVariable Long merchantId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("搜索菜品，商家ID: {}, 关键词: {}, 最低价格: {}, 最高价格: {}, 页码: {}, 每页大小: {}",
                merchantId, keyword, minPrice, maxPrice, page, size);
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = dishService.search(merchantId, keyword, minPrice, maxPrice, page, size);
            logger.info("搜索菜品成功，商家ID: {}, 总数量: {}", merchantId, data.get("total"));
            result.put("code", 200);
            result.put("data", data);
        } catch (Exception e) {
            logger.error("搜索菜品失败，商家ID: {}", merchantId, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable Long id) {
        logger.info("查询菜品详情，菜品ID: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            Object dish = dishService.findById(id);
            logger.info("查询菜品详情成功，菜品ID: {}", id);
            result.put("code", 200);
            result.put("data", dish);
        } catch (Exception e) {
            logger.error("查询菜品详情失败，菜品ID: {}", id, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Dish dish) {
        logger.info("创建菜品，商家ID: {}, 菜品名称: {}", dish.getMerchantId(), dish.getName());
        Map<String, Object> result = new HashMap<>();
        try {
            Dish createdDish = dishService.create(dish);
            logger.info("创建菜品成功，菜品ID: {}", createdDish.getId());
            result.put("code", 200);
            result.put("data", createdDish);
            result.put("message", "菜品创建成功");
        } catch (Exception e) {
            logger.error("创建菜品失败", e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Dish dish) {
        logger.info("更新菜品，菜品ID: {}, 菜品名称: {}", id, dish.getName());
        Map<String, Object> result = new HashMap<>();
        try {
            dish.setId(id);
            Dish updatedDish = dishService.update(dish);
            logger.info("更新菜品成功，菜品ID: {}", id);
            result.put("code", 200);
            result.put("data", updatedDish);
            result.put("message", "菜品更新成功");
        } catch (Exception e) {
            logger.error("更新菜品失败，菜品ID: {}", id, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        logger.info("删除菜品，菜品ID: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            dishService.delete(id);
            logger.info("删除菜品成功，菜品ID: {}", id);
            result.put("code", 200);
            result.put("message", "菜品删除成功");
        } catch (Exception e) {
            logger.error("删除菜品失败，菜品ID: {}", id, e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        logger.info("上传菜品图片，文件名: {}", file.getOriginalFilename());
        Map<String, Object> result = new HashMap<>();
        try {
            // 生成文件名和路径
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String uploadDir = "G:/Deliverydemo/deliveryDemo/uploads/dish/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = uploadDir + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);

            // 保存相对路径到数据库
            String relativePath = "/uploads/dish/" + fileName;
            logger.info("上传菜品图片成功，保存路径: {}", relativePath);
            result.put("code", 200);
            result.put("data", relativePath);
            result.put("message", "图片上传成功");
        } catch (Exception e) {
            logger.error("上传菜品图片失败", e);
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}