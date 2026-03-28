# 外卖点餐系统项目文档

## 1. 项目概述

外卖点餐系统是一个完整的在线点餐平台，支持用户浏览商家、查看菜品、加入购物车、下单支付等功能，同时为商家提供了后台管理系统，支持菜品管理、订单管理、营业状态管理等功能。

### 1.1 项目特点

- **前后端分离架构**：后端使用Spring Boot，前端使用Vue 3 + Element Plus
- **完整的业务流程**：从浏览商家到下单支付的全流程支持
- **商家后台管理**：提供商家专属的管理界面
- **数据安全**：使用JWT进行身份认证，密码加密存储
- **响应式设计**：适配不同设备的显示
- **支付集成**：支持支付宝和微信支付

## 2. 技术栈

### 2.1 后端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 4.0.3 | 应用框架 |
| MyBatis | 3.0.3 | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| Redis | - | 缓存（用于购物车） |
| JWT | 0.12.6 | 身份认证 |
| Spring Security | - | 密码加密 |
| Log4j2 | - | 日志管理 |
| Alipay SDK | 4.38.0.ALL | 支付宝支付集成 |
| Hutool | 5.8.25 | 工具类库 |

### 2.2 前端技术

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5.30 | 前端框架 |
| Vue Router | 4.6.4 | 路由管理 |
| Element Plus | 2.13.5 | UI组件库 |
| Axios | 1.13.6 | HTTP请求 |
| element-china-area-data | 6.1.0 | 省市区选择组件 |
| Vite | 8.0.0 | 构建工具 |

## 3. 项目结构

### 3.1 后端结构

```
deliveryDemo/
├── src/main/
│   ├── java/com/warehouse/deliverydemo/
│   │   ├── config/           # 配置类
│   │   ├── controller/        # 控制器
│   │   ├── entity/            # 实体类
│   │   ├── interceptor/       # 拦截器
│   │   ├── mapper/            # MyBatis映射器
│   │   ├── service/           # 业务逻辑
│   │   │   └── impl/          # 业务逻辑实现
│   │   ├── utils/             # 工具类
│   │   └── DeliveryDemoApplication.java  # 应用入口
│   ├── resources/
│   │   ├── mapper/            # MyBatis XML映射文件
│   │   ├── sql/               # SQL脚本
│   │   ├── application.properties  # 应用配置
│   │   └── log4j2.xml         # 日志配置
├── uploads/                   # 上传文件存储
└── pom.xml                    # Maven配置
```

### 3.2 前端结构

```
frontend/
├── src/
│   ├── assets/                # 静态资源
│   ├── components/            # 组件
│   ├── router/                # 路由配置
│   ├── utils/                 # 工具类
│   ├── views/                 # 页面
│   ├── App.vue                # 根组件
│   ├── main.js                # 入口文件
│   └── style.css              # 全局样式
├── public/                    # 公共资源
├── dist/                      # 构建产物
├── package.json               # 项目配置
└── vite.config.js             # Vite配置
```

## 4. 核心功能

### 4.1 用户端功能

1. **用户注册/登录**：支持用户注册和登录，使用JWT进行身份认证
2. **商家浏览**：展示推荐商家，支持商家搜索，显示商家评分、月售数量、营业状态
3. **菜品查看**：查看商家的菜品列表，支持按分类浏览，查看菜品详情
4. **购物车管理**：添加/删除/修改购物车商品，实时更新购物车数量
5. **地址管理**：添加/编辑/删除收货地址，支持设置默认地址
6. **订单管理**：下单、查看订单列表、查看订单详情、取消订单
7. **支付功能**：集成支付宝和微信支付，支持二维码支付
8. **个人中心**：查看个人信息，修改头像

### 4.2 商家端功能

1. **商家注册/登录**：支持商家注册和登录
2. **店铺管理**：修改店铺信息，上传店铺logo，管理营业状态
3. **菜品管理**：添加/编辑/删除菜品，管理菜品分类，上传菜品图片
4. **订单管理**：查看订单列表，处理订单状态，查看订单详情
5. **营业状态管理**：设置店铺营业/休息状态，与账号状态分离
6. **数据统计**：查看今日订单和收入，查看最近订单
7. **地址显示**：正确显示订单的收货地址和联系电话
8. **支付配置**：设置商家独立的支付宝和微信支付参数

## 5. 数据库设计

### 5.1 核心表结构

#### 5.1.1 用户表（user）

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| id | BIGINT | 用户ID |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(100) | 密码（加密存储） |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(100) | 邮箱 |
| avatar | VARCHAR(255) | 头像路径 |
| role | VARCHAR(20) | 角色（user） |
| status | INT | 状态（1-启用，0-禁用） |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 5.1.2 商家表（merchant）

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| id | BIGINT | 商家ID |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(100) | 密码（加密存储） |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(100) | 邮箱 |
| name | VARCHAR(100) | 店铺名称 |
| address | VARCHAR(255) | 店铺地址 |
| description | TEXT | 店铺描述 |
| logo | VARCHAR(255) | 店铺logo路径 |
| rating | DOUBLE | 评分 |
| sales | INT | 销量 |
| status | INT | 账号状态（1-启用，0-禁用） |
| business_status | INT | 营业状态（1-营业中，0-休息中） |
| alipay_app_id | VARCHAR(50) | 支付宝应用ID |
| alipay_merchant_private_key | TEXT | 支付宝商户私钥 |
| alipay_alipay_public_key | TEXT | 支付宝公钥 |
| wechat_app_id | VARCHAR(50) | 微信应用ID |
| wechat_mch_id | VARCHAR(50) | 微信商户ID |
| wechat_api_key | VARCHAR(100) | 微信API密钥 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 5.1.3 菜品分类表（dish_category）

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| id | BIGINT | 分类ID |
| merchant_id | BIGINT | 商家ID |
| name | VARCHAR(50) | 分类名称 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 5.1.4 菜品表（dish）

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| id | BIGINT | 菜品ID |
| merchant_id | BIGINT | 商家ID |
| category_id | BIGINT | 分类ID |
| name | VARCHAR(100) | 菜品名称 |
| description | TEXT | 菜品描述 |
| price | DOUBLE | 价格 |
| image | VARCHAR(255) | 图片路径 |
| status | INT | 状态（1-上架，0-下架） |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 5.1.5 地址表（address）

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| id | BIGINT | 地址ID |
| user_id | BIGINT | 用户ID |
| name | VARCHAR(50) | 收货人姓名 |
| phone | VARCHAR(20) | 手机号 |
| province | VARCHAR(20) | 省份编码 |
| city | VARCHAR(20) | 城市编码 |
| district | VARCHAR(20) | 区县编码 |
| detail | VARCHAR(255) | 详细地址 |
| is_default | INT | 是否默认（1-是，0-否） |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 5.1.6 订单表（order）

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| id | BIGINT | 订单ID |
| order_no | VARCHAR(50) | 订单号 |
| user_id | BIGINT | 用户ID |
| merchant_id | BIGINT | 商家ID |
| address_id | BIGINT | 地址ID |
| total_amount | DOUBLE | 总金额 |
| status | VARCHAR(20) | 状态（pending-待付款，paid-已支付，shipped-配送中，completed-已完成，cancelled-已取消） |
| shipping_address | VARCHAR(255) | 收货地址 |
| remark | TEXT | 备注 |
| payment_method | VARCHAR(20) | 支付方式 |
| payment_time | DATETIME | 支付时间 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 5.1.7 订单商品表（order_item）

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| id | BIGINT | 商品ID |
| order_id | BIGINT | 订单ID |
| dish_id | BIGINT | 菜品ID |
| quantity | INT | 数量 |
| price | DOUBLE | 单价 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

## 6. API接口

### 6.1 用户相关接口

| 接口路径 | 方法 | 功能 |
|----------|------|------|
| /api/user/register | POST | 用户注册 |
| /api/user/login | POST | 用户登录 |
| /api/user/profile | GET | 获取用户信息 |
| /api/user/profile | PUT | 更新用户信息 |
| /api/user/avatar | POST | 上传头像 |

### 6.2 商家相关接口

| 接口路径 | 方法 | 功能 |
|----------|------|------|
| /api/merchant/register | POST | 商家注册 |
| /api/merchant/login | POST | 商家登录 |
| /api/merchant | GET | 获取商家列表 |
| /api/merchant/{id} | GET | 获取商家详情 |
| /api/merchant/status | PUT | 更新营业状态 |
| /api/merchant/upload | POST | 上传logo |
| /api/merchant/update | PUT | 更新商家信息 |

### 6.3 菜品相关接口

| 接口路径 | 方法 | 功能 |
|----------|------|------|
| /api/dish/categories/{merchantId} | GET | 获取商家菜品分类 |
| /api/dish/category | POST | 创建菜品分类 |
| /api/dish/category/{id} | PUT | 更新菜品分类 |
| /api/dish/category/{id} | DELETE | 删除菜品分类 |
| /api/dish/category/{categoryId} | GET | 获取分类菜品 |
| /api/dish/search/{merchantId} | GET | 搜索菜品 |
| /api/dish/{id} | GET | 获取菜品详情 |
| /api/dish | POST | 创建菜品 |
| /api/dish/{id} | PUT | 更新菜品 |
| /api/dish/{id} | DELETE | 删除菜品 |
| /api/dish/upload | POST | 上传菜品图片 |

### 6.4 地址相关接口

| 接口路径 | 方法 | 功能 |
|----------|------|------|
| /api/address | GET | 获取用户地址列表 |
| /api/address | POST | 创建地址 |
| /api/address/{id} | PUT | 更新地址 |
| /api/address/{id} | DELETE | 删除地址 |
| /api/address/default/{id} | PUT | 设置默认地址 |

### 6.5 购物车相关接口

| 接口路径 | 方法 | 功能 |
|----------|------|------|
| /api/cart | GET | 获取购物车列表 |
| /api/cart/add | POST | 添加商品到购物车 |
| /api/cart/update | POST | 更新购物车商品数量 |
| /api/cart/remove | POST | 从购物车删除商品 |
| /api/cart/clear | POST | 清空购物车 |

### 6.6 订单相关接口

| 接口路径 | 方法 | 功能 |
|----------|------|------|
| /api/order/create | POST | 创建订单 |
| /api/order | GET | 获取用户订单列表 |
| /api/order/{id} | GET | 获取订单详情 |
| /api/order/items/{orderId} | GET | 获取订单商品 |
| /api/order/status/{id} | PUT | 更新订单状态 |
| /api/order/payment/callback | POST | 支付回调 |
| /api/order/pay/alipay | GET | 支付宝支付 |
| /api/order/pay/wechat | GET | 微信支付 |
| /api/merchant/order | GET | 获取商家订单列表 |
| /api/merchant/order/{id} | GET | 获取商家订单详情 |
| /api/merchant/order/items/{orderId} | GET | 获取商家订单商品 |
| /api/merchant/order/status/{id} | PUT | 更新商家订单状态 |

## 7. 部署和运行

### 7.1 环境要求

- JDK 21+
- MySQL 8.0+
- Redis 6.0+
- Node.js 18+
- Maven 3.6+

### 7.2 后端部署

1. **配置数据库**
   - 创建数据库：`CREATE DATABASE delivery_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   - 执行SQL脚本：`src/main/resources/sql/create_tables.sql`
   - 执行SQL脚本：`src/main/resources/sql/add_business_status.sql`
   - 执行SQL脚本：`src/main/resources/sql/add_payment_fields.sql`

2. **配置应用**
   - 修改 `src/main/resources/application.properties` 文件，配置数据库连接和Redis连接

3. **构建项目**
   - 执行 `mvn clean package` 命令构建项目

4. **运行项目**
   - 执行 `java -jar target/deliveryDemo-0.0.1-SNAPSHOT.jar` 命令运行项目

### 7.3 前端部署

1. **安装依赖**
   - 执行 `npm install` 命令安装依赖

2. **构建项目**
   - 执行 `npm run build` 命令构建项目

3. **部署静态资源**
   - 将 `dist` 目录下的文件部署到静态资源服务器

### 7.4 开发环境运行

- **后端**：执行 `mvn spring-boot:run` 命令启动开发服务器
- **前端**：执行 `npm run dev` 命令启动开发服务器

## 8. 常见问题和解决方案

### 8.1 登录问题

- **问题**：商家重新登录后账号被禁用
- **原因**：营业状态和账号状态混淆
- **解决方案**：在Merchant实体中添加了businessStatus字段，区分账号状态和营业状态

### 8.2 图片上传问题

- **问题**：图片上传后显示不了
- **原因**：图片路径处理不正确
- **解决方案**：在前端添加getImageUrl函数，确保图片路径正确

### 8.3 购物车问题

- **问题**：购物车删除商品后还存在
- **原因**：Redis反序列化后键的类型为String，无法直接转换为Long
- **解决方案**：在CartService中添加类型转换处理逻辑

### 8.4 订单问题

- **问题**：订单商品信息缺少菜品名称和图片
- **原因**：OrderService的findOrderItems方法没有添加菜品信息
- **解决方案**：修改OrderService的findOrderItems方法，添加菜品名称和图片信息

### 8.5 地址问题

- **问题**：地址显示为数字编码
- **原因**：存储的是地址编码而非文字
- **解决方案**：创建RegionCodeUtil工具类，将编码转换为文字

### 8.6 月售数量问题

- **问题**：月售数量不正确，没有自动更新
- **原因**：订单支付成功后没有更新商家的月售数量
- **解决方案**：在OrderService的updateOrderStatus和handlePaymentCallback方法中，当订单状态变为"paid"时，调用MerchantService的incrementSales方法自动增加月售数量

### 8.7 营业状态显示问题

- **问题**：商家详情页的营业状态同时显示勾和叉
- **原因**：el-icon标签的使用方式不正确，v-if和v-else指令应该放在el-icon标签上
- **解决方案**：修改el-icon标签的使用方式，将v-if和v-else指令放在el-icon标签上

### 8.8 支付问题

- **问题**：支付宝支付页面显示"请先登录"
- **原因**：支付宝支付接口需要登录认证，但浏览器直接跳转时没有携带token
- **解决方案**：修改支付宝支付接口，使其能够接收token参数并验证用户身份

- **问题**：支付宝支付完成后订单状态没有变化
- **原因**：回调地址配置错误，使用了本地地址，导致支付宝无法成功调用回调
- **解决方案**：使用ngrok将本地服务暴露到公网，并更新回调地址配置

### 8.9 502错误

- **问题**：后端服务返回502 Bad Gateway错误
- **原因**：OrderService.java文件被错误地修改，只保留了部分内容
- **解决方案**：恢复OrderService.java文件的完整内容

### 8.10 订单详情问题

- **问题**：订单详情里没有显示买方的联系电话
- **原因**：OrderService的findById方法没有添加电话号码信息
- **解决方案**：修改OrderService的findById方法，添加address.getPhone()到返回的订单详情中

## 9. 项目维护

### 9.1 日志管理

- 日志文件存储在 `logs/` 目录
- 包含应用日志、错误日志和操作日志

### 9.2 数据备份

- 定期备份数据库
- 备份上传的图片文件

### 9.3 安全管理

- 定期更新依赖库
- 检查并修复安全漏洞
- 监控异常访问

## 10. 未来规划

1. **添加评价功能**：用户可以对订单和商家进行评价
2. **优化推荐算法**：基于用户历史订单推荐商家和菜品
3. **添加优惠券系统**：支持商家发布优惠券
4. **实现实时配送跟踪**：订单配送状态实时更新
5. **添加多语言支持**：支持中英文切换

## 11. 总结

外卖点餐系统是一个功能完整的在线点餐平台，采用前后端分离架构，使用Spring Boot和Vue 3技术栈。系统支持用户端和商家端的完整功能，包括浏览商家、查看菜品、购物车管理、订单管理、支付等核心功能。

项目结构清晰，代码组织合理，易于维护和扩展。通过本项目，我们实现了一个完整的外卖点餐系统，为用户和商家提供了便捷的在线点餐服务。
