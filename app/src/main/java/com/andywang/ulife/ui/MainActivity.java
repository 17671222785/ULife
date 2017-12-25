package com.andywang.ulife.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.LeftMenuItemAdapter;
import com.andywang.ulife.entity.calendar.bean.MenuItemApi;
import com.andywang.ulife.entity.calendar.bean.MenuItemInfo;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.util.style.LanguageChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.view.CalendarFragment;
import com.andywang.ulife.view.MeFragment;
import com.andywang.ulife.view.NoScrollViewPager;
import com.andywang.ulife.view.WeatherFragment;
import com.andywang.ulife.view.fragment.collection.CollectionFragment;
import com.andywang.ulife.view.fragment.joke.JokeFragment;
import com.andywang.ulife.view.fragment.news.NewsFragment;
import com.andywang.ulife.view.fragment.weichat.WeiChatFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @BindView(R.id.rg_main_bottom)
    RadioGroup rgMainBottom;
    @BindView(R.id.vp_main)
    NoScrollViewPager vpMain;
    @BindView(R.id.notify)
    LinearLayout mNotificationHead;
    /**
     * 左上方打开侧滑抽屉的图片
     */
    @BindView(R.id.open_draw)
    ImageButton openDraw;
    /**
     * 应用程序的名字，显示于标题中间
     */
    @BindView(R.id.title_name)
    TextView titleName;
    /**
     * 自定义标题的布局
     */
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;

    @BindView(R.id.main_fragment)
    FrameLayout mainFragment;
    @BindView(R.id.fragment_news_main)
    LinearLayout activityMain;
    /**
     * 左侧圆形图片控件
     */
    @BindView(R.id.login_image)
    CircleImageView loginImage;
    /**
     * 左侧菜单列表UI
     */
    @BindView(R.id.menu_listview)
    ListView menuListView;
    /**
     * 左侧菜单底部设置View
     */
    @BindView(R.id.settings)
    TextView settings;
    /**
     * 左侧菜单底部退出View
     */
    @BindView(R.id.exit)
    TextView exit;

    /**
     * 左侧菜单适配器
     */
    private LeftMenuItemAdapter mMenuAdapter;

    /**
     * 左侧侧滑菜单布局
     */
    @BindView(R.id.left_navigation_layout)
    LinearLayout leftNavigationLayout;
    /**
     * 主布局
     */
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    Unbinder unbinder;
    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    /**
     * 设置
     */
    private Settings mSettings;
    /**
     * 按下返回键的时间
     */
    private long mExitTime;
    /**
     * fragment管理
     */
    private FragmentManager mFragmentManager;
    /**
     * 当前显示的fragment
     */
    private Fragment mCurrentFragment;

    @BindViews({R.id.rb_main_news, R.id.rb_main_weather, R.id.rb_main_calendar, R.id.rb_main_me})
    List<RadioButton> radioButtonList;
    String[] titles = {"新闻", "天气", "日历", "我"};
    List<Integer> botPicList = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setVisibility(View.GONE);
        //初始化底部菜单栏
        initBottomMenu();
        //初始化fragment
        initFragments();
        mNotificationHead = findViewById(R.id.notify);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mNotificationHead.setVisibility(View.VISIBLE);
        }
        mFragmentManager = getSupportFragmentManager();
        mCurrentFragment = new NewsFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment, mCurrentFragment, NewsFragment.NAME).commit();
        initLeftDrawerLayout();
        mSettings = Settings.newsInstance();
        LogUtils.currentLevel = LogUtils.DEBUG;

    }

    /**
     * 给左侧侧滑菜单适配器填充数据源，并把适配器绑定在
     * 菜单列表ListView中<br>
     * 给左上方的图片控件添加监听,触发则打开侧滑左菜单
     */
    private void initLeftDrawerLayout() {
        //给左侧菜单的适配器填充数据项
        mMenuAdapter = new LeftMenuItemAdapter(this, MenuItemApi.getMenuItems());
        menuListView.setAdapter(mMenuAdapter);
        menuListView.setOnItemClickListener(this);
        mMenuAdapter.notifyDataSetChanged();
        loginImage = findViewById(R.id.login_image);

        loginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.startActivity(MainActivity.this);
            }
        });

        //给左上方的图片控件添加监听,触发则打开侧滑左菜单
        openDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(leftNavigationLayout);
            }
        });

        //初始化左侧底部按钮,并设置监听，点击触发不同的事件
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置View被点击，则启动设置的Activity
                SettingsPreferenceActivity.startActivity(MainActivity.this);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment instanceof CollectionFragment && !drawerLayout.isDrawerOpen(leftNavigationLayout)) {
            drawerLayout.openDrawer(GravityCompat.START);
            return;
        } else if (drawerLayout.isDrawerOpen(leftNavigationLayout)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (Settings.is_back_by_twice && (System.currentTimeMillis() - mExitTime) > Settings.EXIT_TIME) {
            mExitTime = System.currentTimeMillis();
            Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
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
            public void onPageScrolled(int arg0, float arg1, int arg2) {
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

    public void setTitleName(int resId) {
        titleName.setText(resId);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItemInfo info = (MenuItemInfo) mMenuAdapter.getItem(position);
        switch (info.getNameId()) {
            case
                    R.string.news:
                //          switchContent(mCurrentFragment, new NewsFragment());
                if (mFragmentManager.findFragmentByTag(NewsFragment.NAME) == null) {
                    mCurrentFragment = new NewsFragment();
                    setCurrentFragment(mCurrentFragment);
                }
                break;
            case R.string.weichat:
                //               switchContent(mCurrentFragment, new WeiChatFragment());
                if (mFragmentManager.findFragmentByTag(WeiChatFragment.NAME) == null) {
                    mCurrentFragment = new WeiChatFragment();
                    setCurrentFragment(mCurrentFragment);
                }
                break;
            case R.string.funny:
                //               switchContent(mCurrentFragment, new FunnyFragment());
                if (mFragmentManager.findFragmentByTag(JokeFragment.NAME) == null) {
                    mCurrentFragment = new JokeFragment();
                    setCurrentFragment(mCurrentFragment);
                }
                break;
            case R.string.collection:
                //               switchContent(mCurrentFragment, new NewsCollectionFragment());
                if (mFragmentManager.findFragmentByTag(CollectionFragment.NAME) == null) {
                    mCurrentFragment = new CollectionFragment();
                    setCurrentFragment(mCurrentFragment);
                }
                break;
            case R.string.night:
                ThemeChangeManager.setNightMode(this);
                MainActivity.this.recreate();
                break;
            case R.string.about:
                AboutActivity.startActivity(this);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * 重启Activity
     */
    public static void refreshActivity(Context context) {
        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(mIntent);
    }

    public void switchContent(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = mFragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.main_fragment, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
                LogUtils.d(CommonInfo.TAG, "--->1");
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void setCurrentFragment(Fragment fragment) {
        if (fragment instanceof NewsFragment) {
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment, NewsFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + NewsFragment.NAME);
        } else if (fragment instanceof WeiChatFragment) {
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment, WeiChatFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + WeiChatFragment.NAME);
        } else if (fragment instanceof JokeFragment) {
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment, JokeFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + JokeFragment.NAME);
        } else if (fragment instanceof CollectionFragment) {
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment, CollectionFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + CollectionFragment.NAME);
        }
    }

    @Override
    public void recreate() {
        try {
            //避免重启太快 恢复
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(mCurrentFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        super.recreate();
    }
}


















































































