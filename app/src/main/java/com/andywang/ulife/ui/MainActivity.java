package com.andywang.ulife.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.MyFragmentAdapter;
import com.andywang.ulife.view.CalendarFragment;
import com.andywang.ulife.view.MeFragment;
import com.andywang.ulife.view.NewsFragment;
import com.andywang.ulife.view.WeatherFragment;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {

    //声明控件
    private ViewPager vpMain;
    private RadioGroup rgMain;
    private MyFragmentAdapter adapter;

    NewsFragment newsFragment;
    WeatherFragment weatherFragment;
    CalendarFragment calendarFragment;
    MeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "83be1f4abc41bdf95639e482f9f575c3");

        //初始化控件

        initialUI();
        //设置监听器
        setListener();

    }

    private void setListener() {

        //为ViewPager设置事件监听
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当所选择的页面发生变化时，获得选择页面在集合当中的位置
                //根据位置改变单选按钮的选择
                switch (position) {
                    case 0:
                        rgMain.check(R.id.rb_main_news);
                        break;
                    case 1:
                        rgMain.check(R.id.rb_main_weather);
                        break;
                    case 2:
                        rgMain.check(R.id.rb_main_calendar);
                        break;
                    case 3:
                        rgMain.check(R.id.rb_main_me);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //为主界面底部按钮设置监听器
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main_news:
                        vpMain.setCurrentItem(0, false);
                        break;
                    case R.id.rb_main_weather:
                        vpMain.setCurrentItem(1, false);
                        break;
                    case R.id.rb_main_calendar:
                        vpMain.setCurrentItem(2, false);
                        break;
                    case R.id.rb_main_me:
                        vpMain.setCurrentItem(3, false);
                        break;
                }
            }
        });

    }

    private void initialUI() {
        vpMain = findViewById(R.id.vp_main);
        rgMain = findViewById(R.id.rg_main_bottom);

        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        newsFragment = new NewsFragment();
        weatherFragment = new WeatherFragment();
        calendarFragment = new CalendarFragment();
        meFragment = new MeFragment();

        adapter.addFragment(newsFragment);
        adapter.addFragment(weatherFragment);
        adapter.addFragment(calendarFragment);
        adapter.addFragment(meFragment);

        //为ViewPager设置监听器
        vpMain.setAdapter(adapter);
        //将联系人的fragment设置为默认的fragment
        vpMain.setCurrentItem(0, false);

    }
}
