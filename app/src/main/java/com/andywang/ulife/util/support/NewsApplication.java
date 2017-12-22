package com.andywang.ulife.util.support;

import android.app.Application;
import android.content.Context;

/**
 * Created by parting_soul on 2016/10/3.
 * 用于随时获得程序的上下文对象
 */

public class NewsApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
