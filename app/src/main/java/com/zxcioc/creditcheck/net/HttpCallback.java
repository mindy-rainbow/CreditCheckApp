package com.zxcioc.creditcheck.net;

/**
 * Created by zhengwen on 16/12/6.
 */

public interface HttpCallback<T> {

    /***
     * 请求成功，httpcode 200,业务code ==1
     *
     * @param request
     * @param data
     */
    public void onResponse(BaseRequest request, Object data);


    /**
     * @param request
     * @param e       异常：网络异常，json解析异常
     * @param code    业务code，若httpcode为200，后两个参数有效；服务端的code,
     * @param message 业务message
     */
    public void onFailure(BaseRequest request, Exception e, int code, String message);
}
