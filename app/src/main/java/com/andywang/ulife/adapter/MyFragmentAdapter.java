package com.andywang.ulife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    //保存要适配Fragment的集合
    private List<Fragment> fragments = new ArrayList<>();

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment){
        if (fragment != null){
            //增加新的Fragment
            fragments.add(fragment);
            //及时更新适配器
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
