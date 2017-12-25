package com.andywang.ulife.util.cache.database;

import android.content.Context;

import com.andywang.ulife.callback.CollectionCallBack;
import com.andywang.ulife.entity.calendar.bean.Joke;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

import org.litepal.LitePalApplication;

import java.util.List;

import static com.andywang.ulife.util.cache.database.DBManager.getDBManager;

/**
 * Created by parting_soul on 2016/11/8.
 */

public class CollectionJokeThread {
    private Context mContext;

    public CollectionJokeThread() {
        mContext = LitePalApplication.getContext();
    }

    public void getCollectionJoke(final CollectionCallBack<Joke> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Joke> lists = getDBManager(mContext).readCollectionJokes();
                callBack.getResult(lists);
            }
        }).start();
    }

    public void setCollectionJoke(final CollectionCallBack<Joke> callBack, final String hashID) {
        LogUtils.d(CommonInfo.TAG, "asdff--->" + "setCollectionNews");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = getDBManager(mContext).addJokeCollectionToDataBase(hashID);
                LogUtils.d(CommonInfo.TAG, "asdff--->" + "run");
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

    public void cancelCollectionJoke(final CollectionCallBack<Joke> callBack, final String hashID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = getDBManager(mContext).deleteJokeCollectionFromDataBase(hashID);
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }
}
