package com.warehouse.deliverydemo.controller;

import com.warehouse.deliverydemo.common.Result;
import com.warehouse.deliverydemo.entity.Dish;
import com.warehouse.deliverydemo.entity.DishCategory;
import com.warehouse.deliverydemo.service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.util.Map;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    private static final Logger logger = LoggerFactory.getLogger(DishController.class);

    @Autowired
    private DishService dishService;

    @GetMapping("/categories/{merchantId}")
    public Result<?> getCategories(@PathVariable Long merchantId) {
        logger.info("查询商家菜品分类，商家ID: {}", merchantId);
        try {
            Object categories = dishService.findCategoriesByMerchantId(merchantId);
            logger.info("查询商家菜品分类成功，商家ID: {}", merchantId);
            return Result.success(categories);
        } catch (Exception e) {
            logger.error("查询商家菜品分类失败，商家ID: {}", merchantId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/category")
    public Result<?> createCategory(@RequestBody DishCategory category) {
        logger.info("创建菜品分类，商家ID: {}, 分类名称: {}", category.getMerchantId(), category.getName());
        try {
            DishCategory createdCategory = dishService.createCategory(category);
            logger.info("创建菜品分类成功，分类ID: {}", createdCategory.getId());
            return Result.success("分类创建成功", createdCategory);
        } catch (Exception e) {
            logger.error("创建菜品分类失败", e);
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/category/{id}")
    public Result<?> updateCategory(@PathVariable Long id, @RequestBody DishCategory category) {
        logger.info("更新菜品分类，分类ID: {}, 分类名称: {}", id, category.getName());
        try {
            category.setId(id);
            DishCategory updatedCategory = dishService.updateCategory(category);
            logger.info("更新菜品分类成功，分类ID: {}", id);
            return Result.success("分类更新成功", updatedCategory);
        } catch (Exception e) {
            logger.error("更新菜品分类失败，分类ID: {}", id, e);
            return Result.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/category/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        logger.info("删除菜品分类，分类ID: {}", id);
        try {
            dishService.deleteCategory(id);
            logger.info("删除菜品分类成功，分类ID: {}", id);
            return Result.success("分类删除成功");
        } catch (Exception e) {
            logger.error("删除菜品分类失败，分类ID: {}", id, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    public Result<?> getByCategory(@PathVariable Long categoryId, @RequestParam Long merchantId) {
        logger.info("查询分类菜品，分类ID: {}, 商家ID: {}", categoryId, merchantId);
        try {
            Object dishes = dishService.findByCategoryId(categoryId, merchantId);
            logger.info("查询分类菜品成功，分类ID: {}, 商家ID: {}", categoryId, merchantId);
            return Result.success(dishes);
        } catch (Exception e) {
            logger.error("查询分类菜品失败，分类ID: {}, 商家ID: {}", categoryId, merchantId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/search/{merchantId}")
    public Result<?> search(@PathVariable Long merchantId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("搜索菜品，商家ID: {}, 关键词: {}, 最低价格: {}, 最高价格: {}, 页码: {}, 每页大小: {}",
                merchantId, keyword, minPrice, maxPrice, page, size);
        try {
            Map<String, Object> data = dishService.search(merchantId, keyword, minPrice, maxPrice, page, size);
            logger.info("搜索菜品成功，商家ID: {}, 总数量: {}", merchantId, data.get("total"));
            return Result.success(data);
        } catch (Exception e) {
            logger.error("搜索菜品失败，商家ID: {}", merchantId, e);
            return Result.serverError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<?> get(@PathVariable Long id) {
        logger.info("查询菜品详情，菜品ID: {}", id);
        try {
            Object dish = dishService.findById(id);
            logger.info("查询菜品详情成功，菜品ID: {}", id);
            return Result.success(dish);
        } catch (Exception e) {
            logger.error("查询菜品详情失败，菜品ID: {}", id, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping
    public Result<?> create(@RequestBody Dish dish) {
        logger.info("创建菜品，商家ID: {}, 菜品名称: {}", dish.getMerchantId(), dish.getName());
        try {
            Dish createdDish = dishService.create(dish);
            logger.info("创建菜品成功，菜品ID: {}", createdDish.getId());
            return Result.success("菜品创建成功", createdDish);
        } catch (Exception e) {
            logger.error("创建菜品失败", e);
            return Result.serverError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Dish dish) {
        logger.info("更新菜品，菜品ID: {}, 菜品名称: {}", id, dish.getName());
        try {
            dish.setId(id);
            Dish updatedDish = dishService.update(dish);
            logger.info("更新菜品成功，菜品ID: {}", id);
            return Result.success("菜品更新成功", updatedDish);
        } catch (Exception e) {
            logger.error("更新菜品失败，菜品ID: {}", id, e);
            return Result.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        logger.info("删除菜品，菜品ID: {}", id);
        try {
            dishService.delete(id);
            logger.info("删除菜品成功，菜品ID: {}", id);
            return Result.success("菜品删除成功");
        } catch (Exception e) {
            logger.error("删除菜品失败，菜品ID: {}", id, e);
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        logger.info("上传菜品图片，文件名: {}", file.getOriginalFilename());
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
            return Result.success("图片上传成功", relativePath);
        } catch (Exception e) {
            logger.error("上传菜品图片失败", e);
            return Result.serverError(e.getMessage());
        }
    }
}