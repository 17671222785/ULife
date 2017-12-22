package com.andywang.ulife.view.fragment.news;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.NewsPageFragmentAdapter;
import com.andywang.ulife.customview.HorizontalNavigation;
import com.andywang.ulife.entity.calendar.bean.NewsKinds;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.util.style.FontChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.WindowSizeTool;
import com.andywang.ulife.view.NewsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parting_soul on 2016/11/1.
 */

public class NewFragment extends Fragment implements ViewPager.OnPageChangeListener, NewsFragment.titleName {
    public static final String NAME = "newsfragment";

    /**
     * 新闻的类型，显示在导航条中
     */
    private static String[] NEWS_TYPES;

    /**
     * 横向导航条的布局
     */
    private LinearLayout mNavigationLayout;

    /**
     * 导航条中导航项的宽度
     */
    private int mNavigation_item_width;

    /**
     * 屏幕宽度(像素)
     */
    private int mScreenWith;

    /**
     * 屏幕能显示菜单项的最大数量
     */
    private static final int itemNum = 7;

    /**
     * 新闻侧滑页面
     */
    private ViewPager mNewsViewPager;

    /**
     * 存放所有新闻页面碎片,用作适配器的数据源
     */
    private List<Fragment> mNewsFragmentLists;

    /**
     * 用于连接viewpager和fragment的适配器
     */
    private NewsPageFragmentAdapter mNewsPagerAdapter;

    /**
     * 用作水平滚动导航条
     */
    private HorizontalNavigation mHorizontalNavigation;

    private int textSizeStyle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_news_fragment, null);
        textSizeStyle = FontChangeManager.changeItemTitleFontSize();
        findById(view);
        initHorizontalNavigationItem();
        initViewPager();
        return view;
    }

    private void findById(View view) {
        mNavigationLayout = view.findViewById(R.id.navigation_layout);
        mNewsViewPager = view.findViewById(R.id.news_viewpager);
        mHorizontalNavigation = view.findViewById(R.id.horizontal_navigation);

        //得到导航条的导航类别名
        NEWS_TYPES = NewsKinds.getNewsTypes();
        //得到屏幕的宽度
        mScreenWith = WindowSizeTool.getScreenWidth(getActivity());
        //计算出每一项的宽度
        mNavigation_item_width = mScreenWith / itemNum;
        //为ViewPager绑定监听器
        mNewsViewPager.addOnPageChangeListener(this);
    }

    /**
     * 根据新闻种类数生成相应数目的fragment,并填充到适配器
     * 且将适配器绑定在ViewPager
     */
    private void initViewPager() {
        mNewsFragmentLists = new ArrayList<Fragment>();
        for (int i = 0; i < NEWS_TYPES.length; i++) {
            NewsDetailFragment fragment = new NewsDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(CommonInfo.NewsAPI.Params.REQUEST_TYPE_PARAM_NAME, NewsKinds.getNewsTypeMap()
                    .get(NEWS_TYPES[i]));
            fragment.setArguments(bundle);
            mNewsFragmentLists.add(fragment);
        }
        mNewsPagerAdapter = new NewsPageFragmentAdapter(getActivity().getSupportFragmentManager(), mNewsFragmentLists);
        mNewsViewPager.setAdapter(mNewsPagerAdapter);
    }

    /**
     * 初始化水平导航<br>
     * <p>根据新闻类别的个数生成相应个数的文本项用于显示新闻类<br>
     * 将每一个生成的TextView动态加入到水平滚动视图用作新闻类别项，为每个新闻项设置监听
     */
    private void initHorizontalNavigationItem() {
        for (int i = 0; i < NEWS_TYPES.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mNavigation_item_width,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;

            //为TextView设置内容,位置,背景,字体等属性
            TextView textView = new TextView(getActivity());
            textView.setClickable(true);
            textView.setText(NEWS_TYPES[i]);
            textView.setGravity(Gravity.CENTER);
            if (Settings.is_night_mode) {
                textView.setBackgroundResource(R.drawable.navigation_item_state_bc_night);
                textView.setTextAppearance(getActivity(),R.style.navigation_item_front_state_style_night);
            } else {
                textView.setBackgroundResource(ThemeChangeManager.getNavigationResoureStateBK());
                textView.setTextAppearance(getActivity(),R.style.navigation_item_front_state_style);
                //             textView.setTextSize();
            }

            textView.setLayoutParams(params);
            //设置唯一的识别标志
            textView.setId(i);
            //默认第一个新闻项为选中状态
            if (i == 0) {
                textView.setSelected(true);
            }
            //为新闻类项设置监听
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //根据父组件找到所有的子组件
                    for (int i = 0; i < mNavigationLayout.getChildCount(); i++) {
                        View view = mNavigationLayout.getChildAt(i);
                        //只要有一个被选中，则改项设为选中 状态,其他应恢复为未选中状态
                        if (view.getId() != v.getId()) {
                            view.setSelected(false);
                        } else {
                            view.setSelected(true);
                            //该类项被选中，跳到对应的新闻fragment
                            mNewsViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            //将textview添加到父组件对应位置
            mNavigationLayout.addView(textView, i);
        }
    }

    /**
     * 根据当前fragment所在的页面号跳转到对应的菜单项目,使得两者对应
     *
     * @param position 当前fragment所在的页面号
     */
    private void navigationToLocation(int position) {
        //得到当前新闻类项
        View currentItem = mNavigationLayout.getChildAt(position);
        //得到该空间距离屏幕最左端的距离
        int itemLeft = currentItem.getLeft();
        //得到该控件的宽度
        int itemWidth = currentItem.getMeasuredWidth();
        //以屏幕中间为起点，若选择的项目超过了屏幕中间，则导航条会滑动
        int x = (itemLeft + itemWidth / 2) - mScreenWith / 2;
        //滑动到x坐标的位置
        mHorizontalNavigation.smoothScrollTo(x, 0);

        for (int i = 0; i < mNavigationLayout.getChildCount(); i++) {
            View view = mNavigationLayout.getChildAt(i);
            if (i == position) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        navigationToLocation(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void setTitleName(int resId) {
        setTitleName(R.string.news);
    }
}
