package com.andywang.ulife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/4.
 * 新闻页面适配器
 */

public class NewsPageFragmentAdapter extends FragmentStatePagerAdapter {

    /**
     * 数据源
     */
    private List<Fragment> mLists;

    public NewsPageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public NewsPageFragmentAdapter(FragmentManager fm, List<Fragment> lists) {
        super(fm);
        mLists = lists;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return mLists.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mLists.size();
    }

}
