package com.andywang.ulife.entity.calendar.bean;


import com.andywang.ulife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parting_soul on 2016/10/3.
 * 左侧菜单的所有项目
 */

public class MenuItemApi {
    /**
     * 所有菜单项的列表
     */
    private static List<MenuItemInfo> mLists;

    static {

        mLists = new ArrayList<MenuItemInfo>();
        MenuItemInfo item = new MenuItemInfo();

        item.setNameId(R.string.news);
        item.setImageId(R.mipmap.news);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.weichat);
        item.setImageId(R.mipmap.weichat);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.funny);
        item.setImageId(R.mipmap.happy);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.collection);
        item.setImageId(R.mipmap.collection);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.night);
        item.setImageId(R.mipmap.ic_night);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.about);
        item.setImageId(R.mipmap.ic_about);
        mLists.add(item);

    }

    public static List<MenuItemInfo> getMenuItems() {
        return mLists;
    }
}
