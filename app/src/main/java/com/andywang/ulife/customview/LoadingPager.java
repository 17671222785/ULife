package com.andywang.ulife.customview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.andywang.ulife.R;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;


/**
 * Created by parting_soul on 2016/10/13.
 * 自定义加载页面
 */

public abstract class LoadingPager extends RelativeLayout {
    /**
     * 正在加载的页面
     */
    private View mLoadingPage;

    /**
     * 网络错误页面
     */
    private View mErrorPage;

    /**
     * 访问成功页面
     */
    private View mSuccessfulPage;

    /**
     * 空白页面
     */
    private View mEmptyPage;

    /**
     * 网络错误时重新加载按钮
     */
    private View mErrorButton;

    /**
     * 未止状态
     */
    public static final int STATE_UNKNOWN = 0x001;

    /**
     * 正在加载状态
     */
    public static final int STATE_LOADING = 0x002;

    /**
     * 网络访问错误状态
     */
    public static final int STATE_ERROR = 0x0003;

    /**
     * 页面为空状态
     */
    public static final int STATE_EMPTY = 0X004;

    /**
     * 获取数据成功，页面成功显示状态
     */
    public static final int STATE_SUCCESS = 0x005;

    /**
     * 当前状态
     */
    private int currentState = STATE_UNKNOWN;

    /**
     * 上下文对象
     */
    private Context mContext;

    public LoadingPager(Context context) {
        this(context, null);
    }

    public LoadingPager(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }


    public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        //      LogUtils.d(CommonInfo.TAG, "-->LoadingPager" + mContext);
    }

    /**
     * 初始化，创建有关页面对象
     */
    private void init() {
        mLoadingPage = findView(R.layout.layout_loading);
        mEmptyPage = findView(R.layout.layout_empty);
        mErrorPage = findView(R.layout.layout_error);
        mErrorButton = mErrorPage.findViewById(R.id.error_button);
        mErrorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        showPages();
    }

    /**
     * 实例化页面对象，并加入布局
     *
     * @param resId
     * @return
     */
    private View findView(int resId) {
        View view = View.inflate(mContext, resId, null);
        if (view != null) {
            addView(view, new RelativeLayout.LayoutParams(LayoutParams.
                    MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        return view;
    }

    /**
     * 初始化当前状态
     */
    public void initCurrentState() {
        currentState = STATE_UNKNOWN;
    }

    /**
     * 根据状态显示不同的的页面
     */
    public void showPages() {
        //当前状态为不知名或者加载状态时，显示加载页面
        mLoadingPage.setVisibility(currentState == STATE_UNKNOWN ||
                currentState == STATE_LOADING ? View.VISIBLE : View.GONE);
        //数据为空时显示空页面
        mEmptyPage.setVisibility(currentState == STATE_EMPTY ? View.VISIBLE : View.GONE);
        //网络错误时显示错误页面
        mErrorPage.setVisibility(currentState == STATE_ERROR ? View.VISIBLE : View.GONE);
        //获取数据成功则显示显示数据界面
        if (currentState == STATE_SUCCESS) {
            mSuccessfulPage = createSuccessPage();
            if (mSuccessfulPage != null) {
                addView(mSuccessfulPage, new RelativeLayout.LayoutParams(LayoutParams.
                        MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mSuccessfulPage.setVisibility(View.VISIBLE);
                //页面显示成功，更新界面中的UI
                updataUI();
                LogUtils.d(CommonInfo.TAG, "success");
            }
        }
    }

    /**
     * 根据从网络或数据库取值结果加载不同的页面
     */
    public void show() {
        if (currentState == STATE_EMPTY || currentState == STATE_ERROR || currentState == STATE_UNKNOWN) {
            currentState = STATE_LOADING;
            showPages();
            LogUtils.d(CommonInfo.TAG, "LoadingPager show current" + currentState);
            new LoadDataAsyncTask().execute();
        }
    }

    /**
     * 数据成功获取时，加载显示数据的页面
     *
     * @return View
     */
    public abstract View createSuccessPage();

    /**
     * 数据显示界面成功显示后更新UI
     */
    public abstract void updataUI();


    /**
     * 从网络或数据库获取数据
     *
     * @return 返回获取状态
     */
    public abstract LoadState loadData();

    /**
     * 加载状态枚举类
     */
    public enum LoadState {
        erro(STATE_ERROR), empty(STATE_EMPTY), success(STATE_SUCCESS);

        private int value;

        LoadState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 异步任务进行加载数据
     */
    class LoadDataAsyncTask extends AsyncTask<Void, Void, LoadState> {


        @SuppressWarnings("WrongThread")
        @Override
        protected LoadState doInBackground(Void... params) {
            return loadData();
        }

        @Override
        protected void onPostExecute(LoadState loadState) {
            super.onPostExecute(loadState);
            //根据加载结果显示相应的页面信息
            if (loadState != null) {
                currentState = loadState.getValue();
                showPages();
            }
        }
    }

}
