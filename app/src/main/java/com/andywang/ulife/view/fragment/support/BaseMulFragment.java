package com.andywang.ulife.view.fragment.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

/**
 * Created by parting_soul on 2016/10/6.
 * 抽象类 使得fragment无法预加载下一页的数据
 */

public abstract class BaseMulFragment<T> extends BaseFragment<T> {


    /**
     * 当前页面是否对用户可见
     */
    protected boolean mIsVisibleToUser;

    /**
     * 加载页面
     */
//    protected LoadingPager mLoadingPage;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisibleToUser = true;
            onVisible();
            LogUtils.d(CommonInfo.TAG, "--->111" + " isVisibleToUser");
        } else {
            mIsVisibleToUser = false;
            onInVisible();
            LogUtils.d(CommonInfo.TAG, "--->111" + " unisVisibleToUser");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mLoadingPage = null;
//        mLoadingPage = new LoadingPager(getActivity()) {
//            @Override
//            public View createSuccessPage() {
//                return BaseMulFragment.this.createSuccessPage();
//            }
//
//            @Override
//            public LoadState loadData() {
//                return BaseMulFragment.this.loadData();
//            }
//
//            @Override
//            public void updataUI() {
//                BaseMulFragment.this.updataUI();
//            }
//        };
        View view = super.onCreateView(inflater, container, savedInstanceState);
        LogUtils.d(CommonInfo.TAG, "--->111onCreateView" + mLoadingPage + " " + mIsPrepared + " " + mIsVisibleToUser);
        //已经布置好UI，可以开始显示页面并下载数据
        mIsPrepared = true;
        show();
        return view;
    }

    /**
     * 当前页面可见时进行的操作
     */
    protected void onVisible() {
        show();
    }

    /**
     * 当前页面不可见时操作
     */
    protected void onInVisible() {

    }

//    /**
//     * 创建显示数据的界面
//     *
//     * @return View
//     */
//    public abstract View createSuccessPage();
//
//    /**
//     * 抽象方法 界面加载成功后在该方法更新UI
//     */
//    public abstract void updataUI();
//
//    /**
//     * 拼接http url的地址参数
//     *
//     * @return String 返回拼接后的参数
//     */
//    protected abstract String initRequestUrlParam();
//
//    /**
//     * 从网络或从数据库加载数据，工作在子线程，不需另外开辟线程
//     */
//    public abstract LoadingPager.LoadState loadData();
//
//    /**
//     * 解析下载的内容，并把数据缓存入数据库
//     *
//     * @param result 带解析的网络数据
//     * @return List<News>
//     */
//    public abstract List<T> parseJsonData(String result);

//    /**
//     * 当布局被销毁时UI准备状态标志位置为false
//     */
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mIsPrepared = false;
//        mLoadingPage.initCurrentState();
//    }

    /**
     * 判断下载数据结果的状态
     *
     * @param lists 下载的结果
     * @return LoadingPager.LoadState 返回下载结果的状态
     */
//    public LoadingPager.LoadState getCheckResult(List<T> lists) {
//        if (lists == null) {
//            //下载失败
//            return LoadingPager.LoadState.erro;
//        } else if (lists.size() == 0) {
//            //数据为空
//            return LoadingPager.LoadState.empty;
//        } else {
//            //下载成功
//            return LoadingPager.LoadState.success;
//        }
//    }

    /**
     * 显示页面加载<br>
     * 在界面可见并且UI已经布置好时加载页面，获取数据
     */
    public void show() {
        if (mLoadingPage != null && mIsPrepared && mIsVisibleToUser) {
            mLoadingPage.show();
            LogUtils.d(CommonInfo.TAG, "--->111" + "basefragment show");
        }
    }
}
