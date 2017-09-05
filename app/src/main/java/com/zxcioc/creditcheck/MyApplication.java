package com.zxcioc.creditcheck;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * @author Admin
 * @description Created on 2017/9/4
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppProfile.init(this);
        //初始化工具类
        //https://github.com/Blankj/AndroidUtilCode
        Utils.init(this);
    }
}
