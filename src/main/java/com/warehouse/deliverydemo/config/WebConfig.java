package com.warehouse.deliverydemo.config;

import com.warehouse.deliverydemo.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
        @Autowired
        private LoginInterceptor loginInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(loginInterceptor)
                                .addPathPatterns("/api/**")
                                .excludePathPatterns("/api/user/register", "/api/user/login", "/api/merchant/register",
                                                "/api/merchant/login", "/api/merchant", "/api/merchant/search",
                                                "/api/merchant/{id}",
                                                "/api/dish/categories/**", "/api/dish/search/**",
                                                "/api/dish/category/{categoryId}",
                                                "/api/dish/{id}",
                                                "/api/order/pay/alipay");

                // 确保商家相关操作接口经过拦截器
                registry.addInterceptor(loginInterceptor)
                                .addPathPatterns("/api/merchant/status", "/api/merchant/update",
                                                "/api/merchant/upload");
        }
}