package com.andywang.ulife.util.support;

import com.andywang.ulife.callback.CollectionCheckStateNotifiyCallBack;

/**
 * Created by parting_soul on 2016/10/31.
 * 收藏状态回调管理类
 */

public class CollectionCheckStateManager {
    /**
     * 来自NewsFragment
     */
    public static final int FROM_NEWSFRAGMENT = 0x1111;

    /**
     * 来自CollectionActivity
     */
    public static final int FROM_COLLECTIONFRAGMENT = 0x1112;

    /**
     * 来自WeiChatFragment
     */
    public static final int FROM_WEICHATFRAGMENT = 0x1122;

    /**
     * 通知可见的NewsFragment更新
     */
    private CollectionCheckStateNotifiyCallBack mNotifyVisibleNewsFragmentCallBack;

    /**
     * 通知CollectionActivity更新
     */
    private CollectionCheckStateNotifiyCallBack mNotifyCollectionActivityCallBack;

    private static CollectionCheckStateManager manager;

    public static CollectionCheckStateManager newInstance() {
        if (manager == null) {
            manager = new CollectionCheckStateManager();
        }
        return manager;
    }

    public CollectionCheckStateNotifiyCallBack getNotifyVisibleNewsFragmentCallBack() {
        return mNotifyVisibleNewsFragmentCallBack;
    }

    public void setNotifyVisibleNewsFragmentCallBack(CollectionCheckStateNotifiyCallBack mNotifyNewsFragmentCallBack) {
        this.mNotifyVisibleNewsFragmentCallBack = mNotifyNewsFragmentCallBack;
    }

    public CollectionCheckStateNotifiyCallBack getNotifyCollectionActivityCallBack() {
        return mNotifyCollectionActivityCallBack;
    }

    public void setNotifyCollectionActivityCallBack(CollectionCheckStateNotifiyCallBack mNotifyCollectionActivityCallBack) {
        this.mNotifyCollectionActivityCallBack = mNotifyCollectionActivityCallBack;
    }

}
