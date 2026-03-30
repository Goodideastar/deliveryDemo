package com.warehouse.deliverydemo.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // 客户端参数错误
    public static <T> Result<T> badRequest() {
        return new Result<>(400, "参数错误", null);
    }

    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }

    // 未登录/登录过期
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未登录或登录过期", null);
    }

    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    // 权限不足
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "权限不足", null);
    }

    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    // 资源不存在
    public static <T> Result<T> notFound() {
        return new Result<>(404, "资源不存在", null);
    }

    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    // 服务器内部异常
    public static <T> Result<T> serverError() {
        return new Result<>(500, "服务器内部异常", null);
    }

    public static <T> Result<T> serverError(String message) {
        return new Result<>(500, message, null);
    }
}