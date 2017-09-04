package com.zxcioc.creditcheck;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * @author Admin
 * @description Created on 2017/9/4
 */
public class MyApplication extends Application {

    public static MyApplication application = null;

    public MyApplication(){
        application =  this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化工具类
        //https://github.com/Blankj/AndroidUtilCode
        Utils.init(this);
    }
}