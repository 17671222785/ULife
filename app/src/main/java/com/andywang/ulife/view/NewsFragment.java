package com.andywang.ulife.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.LeftMenuItemAdapter;
import com.andywang.ulife.entity.calendar.bean.MenuItemApi;
import com.andywang.ulife.entity.calendar.bean.MenuItemInfo;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.ui.AboutActivity;
import com.andywang.ulife.ui.MainActivity;
import com.andywang.ulife.ui.SettingsPreferenceActivity;
import com.andywang.ulife.util.style.LanguageChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.view.fragment.collection.CollectionFragment;
import com.andywang.ulife.view.fragment.joke.JokeFragment;
import com.andywang.ulife.view.fragment.news.NewFragment;
import com.andywang.ulife.view.fragment.weichat.WeiChatFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by andyWang on 2017/10/14 0014.
 * 邮箱：393656489@qq.com
 */

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.notify)
    LinearLayout notify;

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
    ListView menuListview;

    /**
     * 左侧菜单底部设置View
     */
    @BindView(R.id.settings)
    TextView settings;

    /**
     * 左侧菜单适配器
     */
    private LeftMenuItemAdapter mMenuAdapter;

    /**
     * 左侧菜单底部退出View
     */
    @BindView(R.id.exit)
    TextView exit;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        unbinder = ButterKnife.bind(this, view);
        ThemeChangeManager.changeThemeMode(getActivity());
        LanguageChangeManager.changeLanguage();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            notify.setVisibility(View.VISIBLE);
        }
        init();
        initLeftDrawerLayout();
        mSettings = Settings.newsInstance();
        LogUtils.currentLevel = LogUtils.DEBUG;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 进行一些初始化操作
     */
    private void init() {
        mFragmentManager = getActivity().getSupportFragmentManager();

        mCurrentFragment = new NewsFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment, mCurrentFragment, NewFragment.NAME).commit();
    }

    /**
     * 给左侧侧滑菜单适配器填充数据源，并把适配器绑定在
     * 菜单列表ListView中<br>
     * 给左上方的图片控件添加监听,触发则打开侧滑左菜单
     */

    private void initLeftDrawerLayout() {
        //给左侧菜单的适配器填充数据项
        mMenuAdapter = new LeftMenuItemAdapter(getContext(), MenuItemApi.getMenuItems());
        menuListview.setAdapter(mMenuAdapter);
        menuListview.setOnItemClickListener(this);
        mMenuAdapter.notifyDataSetChanged();

        loginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.startActivity(getContext());
            }
        });

        //给左上方的图片控件添加监听,触发则打开侧滑左菜单
        openDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(leftNavigationLayout);
            }
        });
        //初始化左侧底部按钮
        getLeftMenuBottomView();
    }

    /**
     * 找到左侧侧滑菜单底部的设置和退出按钮，并设置监听，点击触发不同的事件
     */
    public void getLeftMenuBottomView() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置View被点击，则启动设置的Activity
                SettingsPreferenceActivity.startActivity(getContext());
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    //    @Override
    //    public void onBackPressed() {
    //        if (mCurrentFragment instanceof CollectionFragment && !mDrawerLayout.isDrawerOpen(mLeft_draw_menu_layout)) {
    //            mDrawerLayout.openDrawer(GravityCompat.START);
    //            return;
    //        } else if (mDrawerLayout.isDrawerOpen(mLeft_draw_menu_layout)) {
    //            mDrawerLayout.closeDrawer(GravityCompat.START);
    //            return;
    //        } else if (Settings.is_back_by_twice && (System.currentTimeMillis() - mExitTime) > Settings.EXIT_TIME) {
    //            mExitTime = System.currentTimeMillis();
    //            Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();
    //            return;
    //        }
    //        super.onBackPressed();
    //    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItemInfo info = (MenuItemInfo) mMenuAdapter.getItem(position);
        switch (info.getNameId()) {
            case R.string.news:
                //          switchContent(mCurrentFragment, new NewsFragment());
                if (mFragmentManager.findFragmentByTag(NewFragment.NAME) == null) {
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
                ThemeChangeManager.setNightMode(getActivity());
                getActivity().recreate();
                break;
            case R.string.about:
                AboutActivity.startActivity(getActivity());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public interface titleName {
        public void setTitleName(int resId);
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
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment, NewFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + NewFragment.NAME);
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

    //    @Override
    //    public void recreate() {
    //        try {
    //            //避免重启太快 恢复
    //            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    //            fragmentTransaction.remove(mCurrentFragment);
    //            fragmentTransaction.commitAllowingStateLoss();
    //        } catch (Exception e) {
    //        }
    //        super.recreate();
    //    }
}
