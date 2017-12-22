package com.andywang.ulife.util.style;

import android.content.Context;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.util.support.NewsApplication;

/**
 * Created by parting_soul on 2016/10/28.
 * 字体大小管理类
 */

public class FontChangeManager {
    public static Context mContext = NewsApplication.getContext();

    public static final String[] fontSizeKey = mContext.getResources().getStringArray(R.array.font_size_key);

    public static int changeItemTitleFontSize() {
        String key = Settings.newsInstance().getString(Settings.FONT_SIZE_KEY, "");
        if (key.equals(fontSizeKey[1])) {
            return R.style.news_item_font_style_normal;
        } else if (key.equals(fontSizeKey[2])) {
            return R.style.news_item_font_style_large;
        } else {
            return R.style.news_item_font_style_small;
        }
    }

    public static int changeJokeFontSize() {
        String key = Settings.newsInstance().getString(Settings.FONT_SIZE_KEY, "");
        if (key.equals(fontSizeKey[1])) {
            return R.style.joke_item_font_style_normal;
        } else if (key.equals(fontSizeKey[2])) {
            return R.style.joke_item_font_style_large;
        } else {
            return R.style.joke_item_font_style_small;
        }
    }

}
