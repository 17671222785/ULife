package com.andywang.ulife.util.support;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by parting_soul on 2016/10/4.
 * 得到窗口像素的工具类
 */

public class WindowSizeTool {
    /**
     * 得到手机屏幕的宽度(像素)
     *
     * @param activity
     * @return int 返回手机屏幕的宽度(像素)
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
