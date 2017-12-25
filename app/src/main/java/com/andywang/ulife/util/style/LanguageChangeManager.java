package com.andywang.ulife.util.style;

import android.content.Context;
import android.content.res.Configuration;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.Settings;

import org.litepal.LitePalApplication;

import java.util.Locale;

/**
 * Created by parting_soul on 2016/10/29.
 */

public class LanguageChangeManager {
    public static Context mContext = LitePalApplication.getContext();

    public static String[] languageKeys = mContext.getResources().getStringArray(R.array.language_key);

    public static void changeLanguage() {
        String key = Settings.newsInstance().getString(Settings.LANUAGE_KEY, "");
        Configuration config = mContext.getResources().getConfiguration();
        if (key.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (key.equals("en")) {
            config.locale = Locale.ENGLISH;
        }
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
    }
}
