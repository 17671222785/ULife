package com.andywang.ulife.entity.calendar.bean;

import com.andywang.ulife.R;

import org.litepal.LitePalApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by parting_soul on 2016/10/3.
 * 新闻的种类，显示在横向导航条上
 */

public class NewsKinds {

    /**
     * 横向导航条上的新闻类别名
     *
     * @return String[]
     */
    public static String[] getNewsTypes() {
        return LitePalApplication.getContext().getResources().getStringArray(R.array.news_type);
    }

    /**
     * 访问问新闻类http地址中的参数
     *
     * @return String[]
     */
    public static String[] getNewsTypeResquestParams() {
        return LitePalApplication.getContext().getResources().getStringArray(R.array.news_type_request_param);
    }

    /**
     * 新闻类别名和协议地址参数的映射
     *
     * @return Map<String,String>
     */
    public static Map<String, String> getNewsTypeMap() {
        Map<String, String> map = new HashMap<String, String>();
        String[] types = getNewsTypes();
        String[] types_params = getNewsTypeResquestParams();
        for (int i = 0; i < types.length; i++) {
            map.put(types[i], types_params[i]);
        }
        return map;
    }

}
