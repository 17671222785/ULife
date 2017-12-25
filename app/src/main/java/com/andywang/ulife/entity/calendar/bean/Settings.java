package com.andywang.ulife.entity.calendar.bean;

import android.content.Context;
import android.content.SharedPreferences;

import org.litepal.LitePalApplication;


/**
 * Created by parting_soul on 2016/10/18.
 * 存放Settings的所有Key
 */

public class Settings {
    /**
     * 存储设置键值对的文件名
     */
    public static final String SETTINGS_XML_NAME = "settings";

    /**
     * 默认为日间模式
     */
    public static boolean is_night_mode = false;

    /**
     * 默认为按两次退出
     */
    public static boolean is_back_by_twice = true;

    /**
     * 默认为非无图模式
     */
    public static boolean is_no_picture_mode = false;

    /**
     * 是否刷新activity
     */
    public static boolean isRefresh = false;

    /**
     * 连按两次返回键的有效间隔时间
     */
    public static long EXIT_TIME = 5000;

    /**
     * 语言选择项的Key
     */
    public static final String LANUAGE_KEY = "language";

    /**
     * 字体选择项的Key
     */
    public static final String FONT_SIZE_KEY = "font_size";

    /**
     * 主题换肤
     */
    public static final String THEME_CHANGE_KEY = "change_theme";

    /**
     * 夜间模式项的Key
     */
    public static final String IS_NIGHT_KEY = "is_night";

    /**
     * 退出确认项的Key
     */
    public static final String BACK_BY_TWICE_KEY = "back_by_twice";

    /**
     * 无图模式的Key
     */
    public static final String NO_PICTURE_KEY = "no_picture";

    /**
     * 清除所有缓存的Key
     */
    public static final String CLEAR_ALL_CACHE = "clean_all_cache";

    /**
     * 重置所有设置
     */
    public static final String RESET_KEY = "reset";

    /**
     * About的KEY
     */
    public static final String ABOUT_KEY = "about";

    /**
     * 单例类
     */
    private static Settings mSettings;

    private SharedPreferences mPreferences;

    /**
     * 返回一个实例
     *
     * @return Settings
     */
    public static Settings newsInstance() {
        if (mSettings == null) {
            mSettings = new Settings(LitePalApplication.getContext());
        }
        return mSettings;
    }

    private Settings(Context context) {
        mPreferences = context.getSharedPreferences(SETTINGS_XML_NAME, Context.MODE_PRIVATE);
        is_no_picture_mode = mPreferences.getBoolean(NO_PICTURE_KEY, false);
        is_night_mode = mPreferences.getBoolean(IS_NIGHT_KEY, false);
        is_back_by_twice = mPreferences.getBoolean(BACK_BY_TWICE_KEY, true);
    }

    public Settings putString(String key, String value) {
        mPreferences.edit().putString(key, value).commit();
        return this;
    }

    public String getString(String key, String def) {
        return mPreferences.getString(key, def);
    }

    public Settings putBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return mPreferences.getBoolean(key, def);
    }

    /**
     * 重置所有设置
     */
    public void initAllSettings() {
        putString(Settings.LANUAGE_KEY, "zh");
        putString(Settings.FONT_SIZE_KEY, "small");
        putString(Settings.THEME_CHANGE_KEY, "red");
        putBoolean(Settings.IS_NIGHT_KEY, false);
        putBoolean(Settings.BACK_BY_TWICE_KEY, true);
        putBoolean(Settings.NO_PICTURE_KEY, false);
        is_no_picture_mode = mPreferences.getBoolean(NO_PICTURE_KEY, false);
        is_night_mode = mPreferences.getBoolean(IS_NIGHT_KEY, false);
        is_back_by_twice = mPreferences.getBoolean(BACK_BY_TWICE_KEY, true);
    }

}
