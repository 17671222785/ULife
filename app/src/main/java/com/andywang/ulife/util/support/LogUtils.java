package com.andywang.ulife.util.support;

import android.util.Log;

/**
 * Created by parting_soul on 2016/10/5.
 * 用于调试的工具类类
 */

public class LogUtils {
    /**
     * log最低等级,打印所有信息
     */
    public static final int VERBOSE = 1;

    /**
     * 打印调试信息
     */
    public static final int DEBUG = 2;

    /**
     * 打印信息
     */
    public static final int INFO = 3;

    /**
     * 打印警告信息
     */
    public static final int WARN = 4;

    /**
     * 打印错误信息
     */
    public static final int ERROR = 5;

    /**
     * 最高等级,什么都不打印
     */
    public static final int NOTHING = 6;

    /**
     * 当前等级
     */
    public static int currentLevel = VERBOSE;

    /**
     * 当前等级小于等于verbose打印所有信息
     *
     * @param TAG
     * @param msg
     */
    public static void v(String TAG, String msg) {
        if (currentLevel <= VERBOSE) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 当前等级小于等于debug打印调试信息
     *
     * @param TAG
     * @param msg
     */
    public static void d(String TAG, String msg) {
        if (currentLevel <= DEBUG) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 当前等级小于等于Info打印信息
     *
     * @param TAG
     * @param msg
     */
    public static void i(String TAG, String msg) {
        if (currentLevel <= INFO) {
            Log.i(TAG, msg);
        }
    }

    /**
     * 当前等级小于等于warn打印警告信息
     *
     * @param TAG
     * @param msg
     */
    public static void w(String TAG, String msg) {
        if (currentLevel <= WARN) {
            Log.w(TAG, msg);
        }
    }

    /**
     * 当前等级小于等于error打印错误信息
     *
     * @param TAG
     * @param msg
     */
    public static void e(String TAG, String msg) {
        if (currentLevel <= ERROR) {
            Log.e(TAG, msg);
        }
    }

}
