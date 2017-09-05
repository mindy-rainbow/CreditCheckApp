package com.zxcioc.creditcheck.net;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zw on 16/10/24.
 */
public abstract class MultiPartRequest<T> extends FormRequest<T> {
    public static final MediaType Media_PART = MediaType.parse("multipart/form-data; charset=utf-8");
    protected List<File> mFiles = new ArrayList<File>();

    public MultiPartRequest() {
        super();
    }

    public MediaType getMediaType() {
        return Media_PART;
    }

    protected Request buildRequest() {
        RequestBody body = null;
        Request.Builder builder = new Request.Builder();

        switch (getHttpMethod()) {
            case HttpMethod.GET:
                //TODO 抛出异常
                break;
            case HttpMethod.POST:
                MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for (File file : mFiles) {
                    if (file.exists()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                        bodyBuilder.addFormDataPart("file", file.getName(), requestBody);
                    }
                }

                for (Map.Entry<String, String> entry : mBodyMap.entrySet()) {
                    if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                        continue;
                    }
                    bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }

                body = bodyBuilder.build();
                builder.url(getUrl()).post(body);
                break;
            default:
                //TODO
                break;
        }
        return builder.build();
    }


}
