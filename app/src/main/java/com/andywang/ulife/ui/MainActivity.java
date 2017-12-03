package com.andywang.ulife.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.view.CalendarFragment;
import com.andywang.ulife.view.MeFragment;
import com.andywang.ulife.view.NewsFragment;
import com.andywang.ulife.view.WeatherFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_toolbar_date)
    TextView tvToolbarDate;
    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.btnRight)
    Button btnRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rg_main_bottom)
    RadioGroup rgMainBottom;
    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindViews({R.id.rb_main_news,R.id.rb_main_weather,R.id.rb_main_calendar,R.id.rb_main_me})
    List<RadioButton> radioButtonList;
    String[] titles = {"新闻", "天气", "日历", "我"};
    List<Integer> botPicList = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ivLeft.setVisibility(View.GONE);
        tvToolbarDate.setVisibility(View.GONE);
        btnRight.setVisibility(View.GONE);

        //初始化底部菜单栏
        initBottomMenu();

        //初始化fragment
        initFragments();

    }


    private void initFragments() {

        fragments.add(new NewsFragment());
        fragments.add(new WeatherFragment());
        fragments.add(new CalendarFragment());
        fragments.add(new MeFragment());

        //设置ViewPager的适配器
        vpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int arg0,float arg1,int arg2) {
                //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用
                //arg0 :当前页面，及你点击滑动的页面
                //arg1:当前页面偏移的百分比
                //arg2:当前页面偏移的像素位置
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转完后被调用，arg0是你当前选中的页面的Position（位置编号)
                updateRadioButtonPicWithIndex(position);
                switch (position) {
                    case 1:
                        toolbar.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //是在状态改变的时候调用
                //state参数有三种状态（0，1，2）
                //arg0 ==1的时表示正在滑动，arg0==2的时表示滑动完毕了，arg0==0的时表示什么都没做
            }
        });
    }

    private void initBottomMenu() {

        botPicList.add(R.drawable.ic_main_new);
        botPicList.add(R.drawable.ic_main_weather);
        botPicList.add(R.drawable.ic_main_calendar);
        botPicList.add(R.drawable.ic_main_me);

        updateRadioButtonPicWithIndex(0);

        rgMainBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //得到点击按钮的下标
                int index = radioGroup.indexOfChild(radioGroup.findViewById(i));
                updateRadioButtonPicWithIndex(index);
                vpMain.setCurrentItem(index);
            }
        });
    }

    private void updateRadioButtonPicWithIndex(int position) {

        //修改页面标题
        tvTitleToolbar.setText(titles[position]);

        //设置RadioButton的颜色
        for (int i = 0; i < radioButtonList.size(); i++) {

            Drawable drawable = getResources().getDrawable(botPicList.get(i));
            drawable.setTint(i == position ? Color.parseColor("#007AFF") : Color.parseColor
                    ("#939393"));
            //设置按钮大小
            drawable.setBounds(0, 0, 45, 45);
            //得到当前遍历的radioButton 设置改变完颜色的图片
            radioButtonList.get(i).setCompoundDrawables(null, drawable, null, null);
            //得到当前遍历的radioButton 设置字体颜色
            radioButtonList.get(i).setTextColor(i == position ? Color.parseColor("#007AFF") :
                    Color.parseColor("#939393"));
        }

    }
}


















































































