package com.andywang.ulife.view.fragment.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.CollectionViewPagerAdapter;
import com.andywang.ulife.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parting_soul on 2016/11/7.
 */

public class CollectionFragment extends Fragment{
    public static final String NAME = "CollectionFragment";

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private CollectionViewPagerAdapter mViewPagerAdapter;

    public static int[] TITLES = {R.string.news, R.string.weichat, R.string.funny};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.collection);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collection_fragment, container, false);
        initViewPager(view);
        return view;
    }

    private void initViewPager(View view) {
        List<Fragment> lists = new ArrayList<Fragment>();
        mViewPager = view.findViewById(R.id.coll_viewpager);
        mTabLayout = view.findViewById(R.id.tab_layout);

        NewsCollectionFragment newsCollectionFragment = new NewsCollectionFragment();
        lists.add(newsCollectionFragment);

        WeiChatCollectionFragment weiChatCollectionFragment = new WeiChatCollectionFragment();
        lists.add(weiChatCollectionFragment);

        JokeCollectionFragment jokeCollectionFragment = new JokeCollectionFragment();
        lists.add(jokeCollectionFragment);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        mTabLayout.addTab(mTabLayout.newTab().setText(TITLES[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(TITLES[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(TITLES[2]));

        mViewPagerAdapter = new CollectionViewPagerAdapter(getChildFragmentManager(), lists);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

}
