package com.andywang.ulife.util.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * Created by parting_soul on 2016/10/7.
 * DiskLruCache的帮助类
 */

public class DiskLruCacheHelper {

    /**
     * 得到缓存的文件对象
     *
     * @param context    上下文
     * @param uniqueName 独一无二的名字
     * @return File 文件对象
     */
    public static File getCacheFile(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //如果sd卡已加载或者不可移除，则得到SD卡中的缓存路径
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //sd卡不可用，得到手机内置存储的缓存路径
            cachePath = context.getCacheDir().getPath();
        }
        //在缓存目录下生成对应的缓存文件
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 得到当前程序的版本号
     *
     * @param context 上下文
     * @return int 版本号
     */
    public static int getVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
