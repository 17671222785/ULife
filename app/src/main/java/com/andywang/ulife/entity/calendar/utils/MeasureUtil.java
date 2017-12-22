package com.andywang.ulife.entity.calendar.utils;

import android.content.Context;

/**
 * 测量工具类
 *
 * Util of measure.
 *
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public final class MeasureUtil {
    private MeasureUtil(){}

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
