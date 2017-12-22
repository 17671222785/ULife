package com.andywang.ulife.util.cache.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

import static com.andywang.ulife.util.cache.database.NewsTable.CREATE_NEWS_TABLE;

/**
 * Created by parting_soul on 2016/10/9.
 * 数据库帮助类
 */

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 数据库名
     */
    public static final String DATABASE_NAME = "database.db";
    /**
     * 数据库版本
     */
    private static final int DATABASE_VERSION = 1;


    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        LogUtils.d(CommonInfo.TAG, CREATE_NEWS_TABLE);
    }

    /**
     * 初始化数据库，创建表
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsTable.CREATE_NEWS_TABLE);
        db.execSQL(WeiChatTable.CREATE_WEICHAT_TABLE);
        db.execSQL(JokeTable.CREATE_JOKE_TABLE);
    }

    /**
     * 升级数据库
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
