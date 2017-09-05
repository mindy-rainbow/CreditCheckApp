package com.zxcioc.creditcheck.net;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zxcioc.creditcheck.AppProfile;
import com.zxcioc.creditcheck.R;
import com.zxcioc.creditcheck.util.ResourceHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zw on 16/8/2.
 */
public abstract class BaseRequest<T> {
    public static final MediaType Media_Json = MediaType.parse("application/json; charset=utf-8");
    protected HashMap<String, String> mQueryMap = new HashMap<String, String>();
    protected HashMap<String, String> mBodyMap = new HashMap<String, String>();
    private OkHttpClient mClient;
    private Call mCall;
    private ExecutorDelivery mDelivery;

    public BaseRequest(HttpClientWrapper httpClient) {
        this.mClient = httpClient.getOkHttpClient();
        this.mDelivery = httpClient.getDelivery();
    }

    public BaseRequest() {
        this(AppProfile.getHttpClientWrapper());
    }

    protected String getHost() {
        return HttpConfig.HOST;
    }

    /***
     * 获取API名称
     *
     * @return
     */
    public abstract String getApi();

    public String getUrl() {
        if (HttpMethod.GET == getHttpMethod()) {
            String queryString = getQueryString();
            if (!TextUtils.isEmpty(queryString)) {
                return String.format("%s%s?%s", getHost(), getApi(), queryString);
            }
        }
        return String.format("%s%s", getHost(), getApi());
    }


    public abstract int getHttpMethod();

    public abstract Class<T> getModelClass();

    public abstract MediaType getMediaType();

    protected Request buildRequest() {
        RequestBody body = null;
        Request.Builder builder = new Request.Builder();
        String strRequest = getRequestString();
        switch (getHttpMethod()) {
            case HttpMethod.GET:
                builder.url(getUrl());

                break;
            case HttpMethod.POST:
                body = RequestBody.create(getMediaType(), strRequest);
                builder.url(getUrl()).post(body);
                break;
            default:
                //TODO
                break;
        }
        return builder.build();

    }


    /***
     * 同步调用
     *
     * @return
     * @throws Exception
     */
    public HttpResponse<T> execute() throws IOException {
        Response response = this.mClient.newCall(buildRequest()).execute();
        int code = response.code();

        if (code == 200) {
            //正常请求
            ResponseBody body = response.body();
            return getResponse(body);

        } else {
            return null;
        }

    }

    private HttpResponse<T> getResponse(ResponseBody body) throws IOException {
        String strBody = body.string();
        Log.e("getResponse", strBody);
//        JSONObject json = JSON.parseObject(strBody);
        HttpResponse<T> httpResponse = new HttpResponse<T>();
//        httpResponse.code = json.getInteger("code");
//        httpResponse.message = json.getString("message");
//        String strData = json.getString("data");
        Object data = JSONObject.parseObject(strBody, getModelClass());
        httpResponse.data = (T) data;
        return httpResponse;
    }

    public final void cancel() {
        if (mCall != null && !mCall.isExecuted()) {
            mCall.cancel();
        }
    }

    public boolean isExecuted() {
        if (mCall != null) {
            return mCall.isExecuted();
        }
        return false;
    }


    public boolean enqueue(final HttpCallback<T> callback) {
        if (mCall == null) {
            mCall = mClient.newCall(buildRequest());
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("enqueue", "onFailure");
                    HttpResponse response = new HttpResponse(ResponseCode.NET_ERROR, ResourceHelper.getString(R.string.neterror), null);
                    mDelivery.postResponse(callback, BaseRequest.this, response, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    HttpResponse<T> httpResponse = null;
                    try {
                        httpResponse = getResponse(response.body());
                        mDelivery.postResponse(callback, BaseRequest.this, httpResponse, null);
                    } catch (IOException e) {
                        httpResponse = new HttpResponse(ResponseCode.NET_ERROR, ResourceHelper.getString(R.string.neterror), null);

                        mDelivery.postResponse(callback, BaseRequest.this, httpResponse, e);
                    } catch (JSONException e) {
                        httpResponse = new HttpResponse(ResponseCode.JSON_ERROR, ResourceHelper.getString(R.string.jsonparseerror), null);
                        mDelivery.postResponse(callback, BaseRequest.this, httpResponse, e);
                    }
                }
            });
        } else {
            if (mCall.isCanceled() || mCall.isExecuted()) {
                Log.e("TAG", "cancel executed");
            }
        }
        return true;

    }

    protected abstract String getRequestString();

    public String getQueryString() {
        if (mQueryMap != null && mQueryMap.size() > 0) {
            return encodeParameters(mQueryMap, "utf-8");
        }
        return null;
    }


    protected String encodeParameters(Map<String, String> params, String paramsEncoding) {
        try {
            if (params != null && params.size() > 0) {
                StringBuilder encodedParams = new StringBuilder();
                int index = 0;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                        continue;
                    }
                    if (index > 0) encodedParams.append('&');
                    encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                    index++;
                }
                return encodedParams.toString();

            } else {
                return null;
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


}
