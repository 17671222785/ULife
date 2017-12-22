package com.andywang.ulife.util.support;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import static android.content.pm.PackageManager.GET_CONFIGURATIONS;

/**
 * Created by parting_soul on 2016/10/30.
 * 系统版本获取工具类
 */

public class VersionManager {

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        PackageManager manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    //系统版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //系统版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

}
