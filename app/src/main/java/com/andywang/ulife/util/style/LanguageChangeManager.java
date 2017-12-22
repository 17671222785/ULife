package com.andywang.ulife.util.style;

import android.content.Context;
import android.content.res.Configuration;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.util.support.NewsApplication;

import java.util.Locale;

/**
 * Created by parting_soul on 2016/10/29.
 */

public class LanguageChangeManager {
    public static Context mContext = NewsApplication.getContext();

    public static String[] languageKeys = mContext.getResources().getStringArray(R.array.language_key);

    public static void changeLanguage() {
        String key = Settings.newsInstance().getString(Settings.LANUAGE_KEY, "");
        Configuration confi = mContext.getResources().getConfiguration();
        if (key.equals("zh")) {
            confi.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (key.equals("en")) {
            confi.locale = Locale.ENGLISH;
        }
        mContext.getResources().updateConfiguration(confi, mContext.getResources().getDisplayMetrics());
    }
}
