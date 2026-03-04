package com.iot.user.exception;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 02:42
 * @Version: 1.0
 */
public class BusinessException extends RuntimeException{

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
