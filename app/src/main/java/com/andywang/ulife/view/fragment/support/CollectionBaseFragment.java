package com.andywang.ulife.view.fragment.support;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.andywang.ulife.callback.CollectionCallBack;
import com.andywang.ulife.callback.CollectionCheckStateNotifiyCallBack;
import com.andywang.ulife.util.network.AbstractDownLoadHandler;
import com.andywang.ulife.util.support.CollectionCheckStateManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

import java.util.List;


/**
 * Created by parting_soul on 2016/11/7.
 */

public abstract class CollectionBaseFragment<T> extends Fragment implements AdapterView.OnItemClickListener,
        CollectionCallBack<T>, CollectionCheckStateNotifiyCallBack {

    protected CollectionCheckStateManager mCollectionCheckStateManager;

    protected List<T> mLists;

    protected T mCurrentSelectedItem;

    protected int currentPos;

    protected ImageView mEmpty;

    public static int FROM_ACTIVITY = 0X1111;

    public static int FROM_JOKE_FRAGMENT = 0X2222;

    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new AbstractDownLoadHandler() {
        @Override
        public void handleMessage(Message msg) {
            updateUI(msg);
        }

        @Override
        protected void showError() {

        }

        @Override
        protected void updateUI(Message msg) {
            CollectionBaseFragment.this.updateUI(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCollectionCheckStateManager = CollectionCheckStateManager.newInstance();
    }


    /**
     * 方法在handleMessage内执行，更新UI
     *
     * @param msg
     */
    public abstract void updateUI(Message msg);

    @Override
    public void getResult(List<T> lists) {
        Message msg = Message.obtain();
        msg.obj = lists;
        msg.what = FROM_ACTIVITY;
        mHandler.sendMessage(msg);
    }


    @Override
    public void isSuccess(boolean isSuccess) {
        Message msg = Message.obtain();
        msg.what = FROM_JOKE_FRAGMENT;
        msg.obj = isSuccess;
        mHandler.sendMessage(msg);
    }

    /**
     * 没有数据时加载空界面
     */
    public void setEmptyView() {
        if (mLists.size() == 0 || mLists == null) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }


    @Override
    public void collectedStateChange(boolean isChange) {
        if (!isChange) {
            mLists.remove(mCurrentSelectedItem);
            LogUtils.d(CommonInfo.TAG, "--->" + mLists.size());
        } else {
            if (!mLists.contains(mCurrentSelectedItem)) {
                mLists.add(currentPos, mCurrentSelectedItem);
            }
        }
        updateFragmentAdapter();
        setEmptyView();
    }

    /**
     * 通知适配器改变数据
     */
    public abstract void updateFragmentAdapter();
}
