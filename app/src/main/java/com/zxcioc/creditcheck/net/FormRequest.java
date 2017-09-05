package com.zxcioc.creditcheck.net;

import okhttp3.MediaType;

/**
 * Created by zw on 16/10/24.
 */
public abstract class FormRequest<T> extends BaseRequest<T> {

    public static final MediaType Media_FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public FormRequest() {
        super();
    }

    public MediaType getMediaType() {
        return Media_FORM;
    }


    protected String getRequestString() {
        if (mBodyMap != null && mBodyMap.size() > 0) {

            try {
                String json = encodeParameters(mBodyMap, "utf-8");
                return json.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
