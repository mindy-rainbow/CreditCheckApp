package com.zxcioc.creditcheck.net;

/**
 * Created by zw on 16/8/3.
 */
public class HttpResponse<T> {
    public Integer code;
    public String message;
    public T data;

    public HttpResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public HttpResponse() {
    }

}
