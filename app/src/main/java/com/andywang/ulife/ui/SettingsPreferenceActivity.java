package com.andywang.ulife.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.util.style.LanguageChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;
import com.andywang.ulife.view.NewsFragment;
import com.andywang.ulife.view.fragment.settings.SettingsPreferenceFragment;

/**
 * Created by parting_soul on 2016/10/18.
 * 用于显示Settings界面的Activity
 */

public class SettingsPreferenceActivity extends AppCompatActivity {

    /**
     * 设置界面
     */
    private SettingsPreferenceFragment mPreferenceFragment;

    /**
     * 返回按钮
     */
    private ImageButton mBackView;

    private LinearLayout mNotificationHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.activity_settings_preference);
        mNotificationHead = findViewById(R.id.notify);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mNotificationHead.setVisibility(View.VISIBLE);
        }
        mBackView = findViewById(R.id.back_forward);
        if (savedInstanceState == null) {
            mPreferenceFragment = new SettingsPreferenceFragment();
            replaceFragment(R.id.settings_container, mPreferenceFragment);
        }
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRefresh();
                SettingsPreferenceActivity.this.finish();
            }
        });

    }

    /**
     * 替换指定布局的view为设置界面的fragment
     *
     * @param viewId
     * @param fragment
     */
    public void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * 其他Activity启动该Activity的接口
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingsPreferenceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        checkRefresh();
        finish();
    }

    /**
     * 判断是否刷新Activity
     */
    public void checkRefresh() {
        if (Settings.isRefresh) {
            NewsFragment.refreshActivity(this);
            Settings.isRefresh = false;
        }
    }

}
