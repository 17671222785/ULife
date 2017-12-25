package com.andywang.ulife.util.cache;

import android.content.Context;

import com.andywang.ulife.callback.CollectionCallBack;
import com.andywang.ulife.entity.calendar.bean.News;
import com.andywang.ulife.util.cache.database.DBManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

import org.litepal.LitePalApplication;

import java.util.List;

import static com.andywang.ulife.util.cache.database.DBManager.getDBManager;

/**
 * Created by parting_soul on 2016/10/31.
 */

public class CollectionNewsThread {
    private Context mContext;

    public CollectionNewsThread() {
        mContext = LitePalApplication.getContext();
    }

    public void getCollectionNews(final CollectionCallBack<News> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> lists = getDBManager(mContext).readCollectionNews();
                callBack.getResult(lists);
            }
        }).start();
    }

    public void setCollectionNews(final CollectionCallBack<News> callBack, final String title) {
        LogUtils.d(CommonInfo.TAG, "asdff--->" + "setCollectionNews");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).addNewsCollectionToDataBase(title);
                LogUtils.d(CommonInfo.TAG, "asdff--->" + "run");
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

    public void cancelCollectionNews(final CollectionCallBack<News> callBack, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).deleteNewsCollectionFromDataBase(title);
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

}