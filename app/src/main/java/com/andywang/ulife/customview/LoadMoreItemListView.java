package com.andywang.ulife.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

/**
 * Created by parting_soul on 2016/11/4.
 */

public class LoadMoreItemListView extends ListView implements AbsListView.OnScrollListener {

    /**
     * 添加在listview末尾的加载更多控件
     */
    private View mFootView;

    /**
     * 总共的item数目
     */
    private int totalItemNum;

    /**
     * 当前页面是否处于加载状态
     */
    private boolean isLoading;

    private Context mContext;

    /**
     * 用于加载下一页的回调接口
     */
    private LoadMoreItemListener mLoadMoreListener;

    /**
     * 加载图片的回调接口
     */
    private LoadImageListener mLoadImageListener;

    /**
     * 加载文字
     */
    private TextView loadText;

    /**
     * 进度条
     */
    private ProgressBar mProgressBar;

    public LoadMoreItemListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        mFootView = LayoutInflater.from(context).inflate(R.layout.loading_next_page, null);
        loadText = mFootView.findViewById(R.id.loading_more);
        mProgressBar = mFootView.findViewById(R.id.loading);
        addFooterView(mFootView);
        setLoadingFinishState();
        this.setOnScrollListener(this);
        LogUtils.d(CommonInfo.TAG, "-->init");
    }

    /**
     * 设置加载下一页数据的监听
     *
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(LoadMoreItemListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    /**
     * 设置加载图片的监听
     *
     * @param mLoadImageListener
     */
    public void setOnLoadImageListener(LoadImageListener mLoadImageListener) {
        this.mLoadImageListener = mLoadImageListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int lastVisibleIndex = view.getLastVisiblePosition();
        LogUtils.d(CommonInfo.TAG, "WeiChat onSrocllState");
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (mLoadImageListener != null) {
                //处于不滑动状态就可以了加载图片
                mLoadImageListener.onLoadImage();
            }
            LogUtils.d(CommonInfo.TAG, "WeiChat LoadImageListener" + totalItemNum + " " + view.getLastVisiblePosition());
            if (!isLoading && lastVisibleIndex == totalItemNum - 1 && scrollState == OnScrollListener.SCROLL_STATE_IDLE && mLoadMoreListener != null) {
                //拉倒最后面时显示进度条，并加载数据
                LogUtils.d(CommonInfo.TAG, "--> on loading");
                isLoading = true;
                setLoadingState();
//                mFootView.setVisibility(View.VISIBLE);

                if (mLoadMoreListener != null) {
                    LogUtils.d(CommonInfo.TAG, "weichat pos" + this.getFirstVisiblePosition() + " " + this.getChildAt(0).getTop());
                    mLoadMoreListener.onLoadMore();
                }
                //               mFootView.setVisibility(View.GONE);
            }
        } else {
            //滑动时通知取消图片的加载
            if (mLoadImageListener != null) {
                mLoadImageListener.onDisLoadImage();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //得到总的item数目
        totalItemNum = totalItemCount;
        LogUtils.d(CommonInfo.TAG, "WeiChat onSrocll");
        if (mLoadImageListener != null) {
            LogUtils.d(CommonInfo.TAG, "WeiChat aa LastVisiblePosition" + view.getLastVisiblePosition() + " " + totalItemCount);
            if (view.getLastVisiblePosition() == totalItemCount - 1) {
                //滑到最后的时候要减去footview这一项
                LogUtils.d(CommonInfo.TAG, "WeiChat aa firstVisibleItem" + firstVisibleItem + " visibleItemCount" + visibleItemCount);
                mLoadImageListener.onLoadImage(firstVisibleItem, visibleItemCount - 1);
            } else {
                mLoadImageListener.onLoadImage(firstVisibleItem, visibleItemCount);
            }
        }
    }

    /**
     * 加载状态显示控件
     */
    private void setLoadingState() {
        mProgressBar.setVisibility(View.VISIBLE);
        loadText.setVisibility(View.VISIBLE);
    }

    /**
     * 完成加载时隐藏控件
     */
    private void setLoadingFinishState() {
        mProgressBar.setVisibility(View.GONE);
        loadText.setVisibility(View.GONE);
    }

    /**
     * 加载完成后必须调用
     */
    public void setLoadCompleted() {
        isLoading = false;
        //       removeFooterView(mFootView);
        setLoadingFinishState();
    }


    /**
     * 回调接口，通知可以加载更多数据
     */
    public interface LoadMoreItemListener {
        public void onLoadMore();
    }

    /**
     * 回调接口，通知是否可以加载图片
     */
    public interface LoadImageListener {
        /**
         * 通知加载图片
         */
        public void onLoadImage();

        /**
         * 通知加载图片
         *
         * @param firstVisibleItem
         * @param visibleItemCount
         */
        public void onLoadImage(int firstVisibleItem, int visibleItemCount);

        /**
         * 通知取消加载图片
         */
        public void onDisLoadImage();
    }

}
