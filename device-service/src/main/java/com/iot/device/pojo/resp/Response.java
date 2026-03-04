package com.iot.device.pojo.resp;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 11:47
 * @Version: 1.0
 */
@Getter
@Setter
public class Response<T> {

    private int code;
    private String message;
    private T data;

    public Response() {
        this.code = 200;
    }

    public Response(T data) {
        this.code = 200;
        this.data = data;
    }

    public Response(String message) {
        this.code = 500;
        this.message = message;
    }

    public static <T> Response<T> success() {
        return new Response<>();
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(data);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(message);
    }
}
