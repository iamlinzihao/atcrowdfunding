package com.atguigu.crowd.exception;

/**
 * 用户没有登录就访问受保护的资源抛出的异常
 * @author linzihao
 * @create 2022-10-08-23:39
 */
public class AccessForbiddenException extends RuntimeException{
    public AccessForbiddenException() {
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    public AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
