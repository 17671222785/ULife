package com.andywang.ulife.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andywang.ulife.R;
import com.andywang.ulife.callback.CollectionCallBack;
import com.andywang.ulife.entity.calendar.bean.News;
import com.andywang.ulife.entity.calendar.bean.WeiChat;
import com.andywang.ulife.util.cache.CollectionNewsThread;
import com.andywang.ulife.util.cache.CollectionWeiChatThread;
import com.andywang.ulife.util.style.LanguageChangeManager;
import com.andywang.ulife.util.style.ThemeChangeManager;
import com.andywang.ulife.util.support.CollectionCheckStateManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.util.support.NewsApplication;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.andywang.ulife.util.support.CollectionCheckStateManager.FROM_COLLECTIONFRAGMENT;
import static com.andywang.ulife.util.support.CollectionCheckStateManager.FROM_NEWSFRAGMENT;
import static com.andywang.ulife.util.support.CollectionCheckStateManager.FROM_WEICHATFRAGMENT;

/**
 * Created by andyWang on 2017/10/14 0014.
 * 显示新闻内容的Activity
 */
public class MessageActivity extends AppCompatActivity implements View.OnClickListener
        , CompoundButton.OnCheckedChangeListener {

    /**
     * url的Key
     */
    private static final String URL_KEY = "url";

    /**
     * title的key
     */
    private static final String TITLE_KEY = "title";

    /**
     * 新闻是否被收藏
     */
    private static final String IS_COLLECTED = "is_collected";

    /**
     * 收藏结束
     */
    private static final int COLLECTION_FINISH = 0X2222;

    /**
     * 取消收藏结束
     */
    private static final int DIS_COLLECTION_FINISH = 0X333;

    private LinearLayout mNotificationHead;

    /**
     * 显示网页新闻
     */
    private WebView mWebView;

    /**
     * 标题中的返回按钮
     */
    private ImageButton mBackView;

    /**
     * 收藏单选框
     */
    private static CheckBox mIsCollected;

    /**
     * 新闻url
     */
    private String mUrl;

    /**
     * 新闻标题
     */
    private String mTitle;

    /**
     * 是否被收藏
     */
    private boolean is_Collected;

    private TextView mTitleView;

    /**
     * 收藏回调接口管理类
     */
    private static CollectionCheckStateManager mCalllBackManager = CollectionCheckStateManager.newInstance();

    private static int from_where_activity;

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COLLECTION_FINISH:
                    judgeResult(msg, R.string.is_collected);
                    break;
                case DIS_COLLECTION_FINISH:
                    judgeResult(msg, R.string.cancel_collected);
                    break;
            }
        }

        private void judgeResult(Message msg, int is_collected) {
            if ((Boolean) (msg.obj)) {
                Toast.makeText(NewsApplication.getContext(), is_collected, Toast.LENGTH_SHORT).show();
                //若是从CollectionActivity则通知其更新
                if (from_where_activity == FROM_COLLECTIONFRAGMENT) {
                    mCalllBackManager.getNotifyCollectionActivityCallBack().collectedStateChange(mIsCollected.isChecked());
                } else if (from_where_activity == FROM_NEWSFRAGMENT) {
                    //通知newsfragment将该新闻变为当前收藏的状态
                    mCalllBackManager.getNotifyVisibleNewsFragmentCallBack().collectedStateChange(mIsCollected.isChecked());
                } else if (from_where_activity == FROM_WEICHATFRAGMENT) {
                    mCalllBackManager.getNotifyVisibleNewsFragmentCallBack().collectedStateChange(mIsCollected.isChecked());
                }
            } else {
                mIsCollected.setChecked(!mIsCollected.isChecked());
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.activity_message);
        mNotificationHead = (LinearLayout) findViewById(R.id.notify);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mNotificationHead.setVisibility(View.VISIBLE);
        }
        mTitleView = (TextView) findViewById(R.id.title_name);
        changeTitle();
        mBackView = (ImageButton) findViewById(R.id.back_forward);
        mBackView.setOnClickListener(this);
        mIsCollected = (CheckBox) findViewById(R.id.is_collectedBox);
        mWebView = (WebView) findViewById(R.id.webView);
        mIsCollected.setOnCheckedChangeListener(this);

        Intent result = getIntent();
        if (result != null) {
            mUrl = result.getStringExtra(URL_KEY);
            mTitle = result.getStringExtra(TITLE_KEY);
            is_Collected = result.getBooleanExtra(IS_COLLECTED, false);
            mIsCollected.setChecked(is_Collected);
            LogUtils.d(CommonInfo.TAG, "--->" + mTitle);

            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            mWebView.setWebChromeClient(new WebChromeClient());

            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.loadUrl(mUrl);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    private void changeTitle() {
        switch (from_where_activity) {
            case FROM_NEWSFRAGMENT:
                mTitleView.setText(R.string.news);
                break;
            case FROM_WEICHATFRAGMENT:
                mTitleView.setText(R.string.weichat);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //为了使WebView退出时音频或视频关闭
        mWebView.destroy();
    }

    /**
     * 其他activity启动该Activity的接口
     *
     * @param context 启动该activity的上下文
     * @param url     要显示新闻的url地址
     */
    public static void startActivity(Context context, String url, String title, boolean isCollected, int fromWhere) {
        Intent intent = new Intent();
        intent.putExtra(URL_KEY, url);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(IS_COLLECTED, isCollected);
        intent.setClass(context, MessageActivity.class);
        from_where_activity = fromWhere;
        context.startActivity(intent);
    }

    /**
     * 若当前页面可返回到上一页且按下了返回键，则返回上一页
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView != null && mWebView.canGoBack()) {
            if (mWebView.getUrl().equals(mUrl)) {
                return super.onKeyDown(keyCode, event);
            } else {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 触发返回事件，销毁该Activity
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        this.finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        mIsCollected.setChecked(isChecked);
        if (isChecked) {
            if (!is_Collected) {
                LogUtils.d(CommonInfo.TAG, "asdff" + isChecked);
                judgeFromWhereFragment(isChecked);
            }
        } else {
            judgeFromWhereFragment(isChecked);
            is_Collected = false;
        }
    }

    public void judgeFromWhereFragment(boolean isCollected) {
        if (from_where_activity == FROM_WEICHATFRAGMENT) {
            if (isCollected) {
                addWeiChatCollection();
            } else {
                cancelWeiChatCollection();
            }
        } else if (from_where_activity == FROM_NEWSFRAGMENT) {
            if (isCollected) {
                addNewsColllection();
            } else {
                cancelNewsCollection();
            }
        }
    }

    public void addNewsColllection() {
        new CollectionNewsThread().setCollectionNews(new CollectionCallBack<News>() {
            @Override
            public void getResult(List<News> newsList) {

            }

            @Override
            public void isSuccess(boolean isSuccess) {
                Message msg = Message.obtain();
                msg.obj = isSuccess;
                msg.what = COLLECTION_FINISH;
                mHandler.sendMessage(msg);
            }
        }, mTitle);
    }

    public void cancelNewsCollection() {
        new CollectionNewsThread().cancelCollectionNews(new CollectionCallBack<News>() {
            @Override
            public void getResult(List<News> newsList) {

            }

            @Override
            public void isSuccess(boolean isSuccess) {
                Message msg = Message.obtain();
                msg.obj = isSuccess;
                msg.what = DIS_COLLECTION_FINISH;
                mHandler.sendMessage(msg);
            }
        }, mTitle);
    }

    public void addWeiChatCollection() {
        new CollectionWeiChatThread().setCollectionWeiChat(new CollectionCallBack<WeiChat>() {
            @Override
            public void getResult(List<WeiChat> newsList) {

            }

            @Override
            public void isSuccess(boolean isSuccess) {
                Message msg = Message.obtain();
                msg.obj = isSuccess;
                msg.what = COLLECTION_FINISH;
                mHandler.sendMessage(msg);
            }
        }, mTitle);
    }

    public void cancelWeiChatCollection() {
        new CollectionWeiChatThread().cancelCollectionWeiChat(new CollectionCallBack<WeiChat>() {
            @Override
            public void getResult(List<WeiChat> newsList) {

            }

            @Override
            public void isSuccess(boolean isSuccess) {
                Message msg = Message.obtain();
                msg.obj = isSuccess;
                msg.what = DIS_COLLECTION_FINISH;
                mHandler.sendMessage(msg);
            }
        }, mTitle);
    }
}
