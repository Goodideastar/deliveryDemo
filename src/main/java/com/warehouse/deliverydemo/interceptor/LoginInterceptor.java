package com.warehouse.deliverydemo.interceptor;

import com.warehouse.deliverydemo.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("拦截请求: {}", request.getRequestURI());

        // 从请求头中获取Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            logger.warn("未提供Token: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": 401, \"message\": \"未登录\"}");
            return false;
        }

        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            logger.warn("Token无效: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": 401, \"message\": \"Token无效\"}");
            return false;
        }

        // 从Token中获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        logger.info("认证成功，用户ID: {}, 请求: {}", userId, request.getRequestURI());
        return true;
    }
}