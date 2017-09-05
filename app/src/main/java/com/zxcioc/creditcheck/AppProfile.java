package com.zxcioc.creditcheck;

import android.content.Context;

import com.zxcioc.creditcheck.net.HttpClientWrapper;


/**
 * Created by cm on 2017/1/31.
 */

public class AppProfile {
    private static Context sApplicationContext;
    private static HttpClientWrapper sHttpClientWrapper;

    public static HttpClientWrapper getHttpClientWrapper() {
        return sHttpClientWrapper;
    }

    public static Context getContext() {
        return sApplicationContext;
    }

    public static void init(Context context) {
        sApplicationContext = context;
        sHttpClientWrapper = new HttpClientWrapper(context);
    }
}
