package com.andywang.ulife.util.style;

import android.app.Activity;
import android.content.Context;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.util.support.NewsApplication;

import java.util.HashMap;
import java.util.Map;

import static com.andywang.ulife.util.style.ThemeChangeManager.Color.BLACK;
import static com.andywang.ulife.util.style.ThemeChangeManager.Color.BLUE;
import static com.andywang.ulife.util.style.ThemeChangeManager.Color.GREEN;
import static com.andywang.ulife.util.style.ThemeChangeManager.Color.PURPLE;
import static com.andywang.ulife.util.style.ThemeChangeManager.Color.RED;

/**
 * Created by parting_soul on 2016/10/24.
 * Theme切换管理类
 */

public class ThemeChangeManager {
    public static Color currentColor;

    public static Map<String, Color> map;

    public static Context mContext = NewsApplication.getContext();

    public static String[] colorKeys = mContext.getResources().getStringArray(R.array.theme_key);

    static {
        map = new HashMap<String, Color>();
        map.put(colorKeys[0], RED);
        map.put(colorKeys[1], BLUE);
        map.put(colorKeys[2], GREEN);
        map.put(colorKeys[3], PURPLE);
        map.put(colorKeys[4], BLACK);
    }

    public enum Color {
        RED, GREEN, BLUE, PURPLE, BLACK
    }

    public static void changeTitleTheme(Activity activity) {
        currentColor = map.get(Settings.newsInstance().getString(Settings.THEME_CHANGE_KEY, ""));
        LogUtils.d(CommonInfo.TAG, "-->" + currentColor);
        if (currentColor != null) {
            switch (currentColor) {
                case RED:
                    activity.setTheme(R.style.DayTheme_red);
                    break;
                case GREEN:
                    activity.setTheme(R.style.DayTheme_green);
                    break;
                case BLUE:
                    activity.setTheme(R.style.DayTheme_blue);
                    break;
                case PURPLE:
                    activity.setTheme(R.style.DayTheme_purple);
                    break;
                case BLACK:
                    activity.setTheme(R.style.DayTheme_black);
                    break;
            }
        }
    }

    /**
     * 夜间模式更换成功则返回true
     *
     * @param activity
     * @return
     */
    public static boolean changeDayNightMode(Activity activity) {
        boolean is_night_mode = Settings.is_night_mode = Settings.newsInstance().
                getBoolean(Settings.IS_NIGHT_KEY, false);
        LogUtils.d(CommonInfo.TAG, "--->55555" + is_night_mode);
        if (is_night_mode) {
            activity.setTheme(R.style.NightTheme);
            return true;
        }
        return false;
    }

    public static void setNightMode(Activity activity) {
        Settings.is_night_mode = !Settings.is_night_mode;
        Settings.newsInstance().putBoolean(Settings.IS_NIGHT_KEY, Settings.is_night_mode);
    }

    public static int getNavigationResoureStateBK() {
        currentColor = map.get(Settings.newsInstance().getString(Settings.THEME_CHANGE_KEY, ""));
        if (currentColor != null) {
            switch (currentColor) {
                case GREEN:
                    return R.drawable.navigation_item_state_bc_green;
                case BLUE:
                    return R.drawable.navigation_item_state_bc_blue;
                case PURPLE:
                    return R.drawable.navigation_item_state_bc_purple;
                case BLACK:
                    return R.drawable.navigation_item_state_bc_black;
                case RED:
                default:
                    return R.drawable.navigation_item_state_bc_red;
            }
        }
        return R.drawable.navigation_item_state_bc_red;
    }

    public static void changeThemeMode(Activity activity) {
        if (!changeDayNightMode(activity)) {
            changeTitleTheme(activity);
        }
    }

}
