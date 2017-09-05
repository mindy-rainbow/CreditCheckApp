package com.zxcioc.creditcheck.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import okhttp3.MediaType;

/**
 * Created by zw on 16/8/2.
 */
public abstract class JSONRequest<T> extends BaseRequest<T> {
    public static final MediaType Media_Json = MediaType.parse("application/json; charset=utf-8");

    public JSONRequest(HttpClientWrapper wrapper) {
        super(wrapper);
    }

    public MediaType getMediaType() {
        return Media_Json;
    }

    protected String getRequestString() {
        if (mBodyMap != null && mBodyMap.size() > 0) {
            try {
                String json = JSON.toJSONString(mBodyMap, SerializerFeature.WriteNullStringAsEmpty);
                return json.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
