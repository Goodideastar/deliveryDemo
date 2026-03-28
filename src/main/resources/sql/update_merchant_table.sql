-- 更新商家表结构
-- 先删除旧表（如果存在）
DROP TABLE IF EXISTS `dish`;
DROP TABLE IF EXISTS `dish_category`;
DROP TABLE IF EXISTS `merchant`;

-- 创建新的商家表
CREATE TABLE `merchant` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(20) NOT NULL,
  `email` VARCHAR(50),
  `name` VARCHAR(100) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `description` TEXT,
  `logo` VARCHAR(255),
  `rating` DECIMAL(3,2) DEFAULT 0,
  `sales` INT DEFAULT 0,
  `status` INT DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 重新创建菜品分类表
CREATE TABLE `dish_category` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `merchant_id` BIGINT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `sort` INT DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`merchant_id`) REFERENCES `merchant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 重新创建菜品表
CREATE TABLE `dish` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `merchant_id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `price` DECIMAL(10,2) NOT NULL,
  `stock` INT NOT NULL,
  `image` VARCHAR(255),
  `status` INT DEFAULT 1,
  `sort` INT DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`merchant_id`) REFERENCES `merchant`(`id`),
  FOREIGN KEY (`category_id`) REFERENCES `dish_category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
