package com.atguigu.crowd.exception;

/**
 * 登陆失败后抛出的异常
 * @author linzihao
 * @create 2022-10-08-16:52
 */
public class LoginFailedException extends RuntimeException{

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
