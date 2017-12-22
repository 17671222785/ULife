package com.andywang.ulife.util.cache.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.andywang.ulife.entity.calendar.bean.Joke;
import com.andywang.ulife.entity.calendar.bean.News;
import com.andywang.ulife.entity.calendar.bean.WeiChat;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.andywang.ulife.util.cache.database.JokeTable.JOKE_TABLE_CONTENT;
import static com.andywang.ulife.util.cache.database.JokeTable.JOKE_TABLE_ID;
import static com.andywang.ulife.util.cache.database.JokeTable.JOKE_TABLE_IS_COLLECTED;
import static com.andywang.ulife.util.cache.database.JokeTable.JOKE_TABLE_NAME;
import static com.andywang.ulife.util.cache.database.JokeTable.JOKE_TABLE_PAGE;
import static com.andywang.ulife.util.cache.database.JokeTable.JOKE_TABLE_UPDATA_TIME;
import static com.andywang.ulife.util.cache.database.NewsTable.NEWS_TABLE_AUTHOR_NAME;
import static com.andywang.ulife.util.cache.database.NewsTable.NEWS_TABLE_DATE;
import static com.andywang.ulife.util.cache.database.NewsTable.NEWS_TABLE_IS_COLLECTION;
import static com.andywang.ulife.util.cache.database.NewsTable.NEWS_TABLE_NAME;
import static com.andywang.ulife.util.cache.database.NewsTable.NEWS_TABLE_PICPATH;
import static com.andywang.ulife.util.cache.database.NewsTable.NEWS_TABLE_TITLE;
import static com.andywang.ulife.util.cache.database.NewsTable.NEWS_TABLE_URL;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_IS_COLLECTED;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_PAGE;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_TABLE_ID;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_TABLE_NAME;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_TABLE_PIC_PATH;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_TABLE_TITLE;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_TABLE_URL;
import static com.andywang.ulife.util.cache.database.WeiChatTable.WEICHAT_TABLE__SOURCE;

/**
 * Created by parting_soul on 2016/10/9.
 * 数据库管理类
 */

public class DBManager {


    /**
     * 数据库帮助类
     */
    private SQLiteDatabaseHelper helper;

    /**
     * 数据库对象
     */
    private static SQLiteDatabase database;

    /**
     * 保证只有一个数据库管理类
     */
    private static DBManager manager;

    /**
     * 是否是第一次打开数据库
     */
    public static boolean isFirst = true;

    /**
     * 数据库为空时的文件大小
     */
    public static long databaseSize;

    private Context mContext;

    private DBManager(Context context) {
        helper = new SQLiteDatabaseHelper(context);
        mContext = context;
    }

    public static DBManager getDBManager(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    /**
     * 打开或创建数据库,并得到数据库为空时的文件大小
     */
    public void getConnected() {
        database = helper.getReadableDatabase();
        if (isFirst) {
            databaseSize = mContext.getDatabasePath(SQLiteDatabaseHelper.DATABASE_NAME).length();
            LogUtils.d(CommonInfo.TAG, "--->123 " + databaseSize);
            isFirst = false;
        }
    }

    /**
     * 关闭数据库
     */
    public void disConnected() {
        database.close();
    }

    /**
     * 添加新闻缓存到数据库，存在就忽略插入
     *
     * @param news
     * @param newsType
     */
    public void addNewsCacheToDataBase(List<News> news, String newsType) {
        getConnected();
        long re = -1;
        if (news != null && newsType != null) {
            for (News n : news) {
                StringBuilder sql = new StringBuilder();
                sql.append("insert or replace into ").append(NewsTable.NEWS_TABLE_NAME).append(" ( ")
                        .append(NewsTable.NEWS_TABLE_TITLE + "," + NewsTable.NEWS_TABLE_PICPATH + "," + NewsTable.NEWS_TABLE_AUTHOR_NAME
                                + "," + NewsTable.NEWS_TABLE_URL + "," + NewsTable.NEWS_TABLE_DATE + "," + NewsTable.NEWS_TABLE_NEWS_TYPE + "," + NewsTable.NEWS_TABLE_IS_COLLECTION)
                        .append(" ) values( ").append(" ? ").append("," + " ? " + "," + " ? "
                        + "," + " ? " + "," + " ? " + "," + " ? ").append(",(").append("select ")
                        .append(NewsTable.NEWS_TABLE_IS_COLLECTION).append(" from ").append(NewsTable.NEWS_TABLE_NAME).append(" where ").append(NewsTable.NEWS_TABLE_TITLE).append(" = " + " ? " + ") )");
                database.execSQL(sql.toString(), new Object[]{n.getTitle(), n.getPicPath(), n.getAuthor_name(), n.getUrl(), n.getDate(), newsType, n.getTitle()});
                LogUtils.d(CommonInfo.TAG, "--->add news cache" + sql.toString());
            }

        }
    }

    /**
     * 移除所有新闻缓存
     *
     * @param newsType
     */
    public void deleteNewsCacheFromDataBase(String newsType) {
        getConnected();
        database.delete(NewsTable.NEWS_TABLE_NAME, NewsTable.NEWS_TABLE_NEWS_TYPE + " = ? and (" + NewsTable.NEWS_TABLE_IS_COLLECTION + " = ? or  "
                + NEWS_TABLE_IS_COLLECTION + " is null )", new String[]{newsType, "0"});
    }

    /**
     * 移除所有数据缓存
     */
    public boolean deleteAllCacheFromDataBase() {
        getConnected();
        boolean isSuccess = false;
        database.beginTransaction();
        try {
            database.execSQL(NewsTable.DELETE_NEWS_TABLE_CACHE, new String[]{"0"});
            LogUtils.d(CommonInfo.TAG, "database delte news --" + NewsTable.DELETE_NEWS_TABLE_CACHE);
            database.execSQL(WeiChatTable.DELETE_WEICHAT_DATA, new String[]{"0"});
            LogUtils.d(CommonInfo.TAG, "database delte weichat -- " + WeiChatTable.DELETE_WEICHAT_DATA);
            database.execSQL(JokeTable.DELETE_ALL_JOKE_CACHE, new String[]{"0"});

            //设置事务标志为成功，当结束事务时就会提交事务
            database.setTransactionSuccessful();
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
        } finally {
            database.endTransaction();
        }
        LogUtils.d(CommonInfo.TAG, "database deleteall " + isSuccess);
        return true;
    }


    /**
     * 从数据库读取新闻缓存
     *
     * @param newsType 新闻类型
     * @return List<News> 新闻缓存项
     */
    public List<News> readNewsCacheFromDatabase(String newsType) {
        getConnected();
        Cursor cursor = database.query(NewsTable.NEWS_TABLE_NAME, null, NewsTable.NEWS_TABLE_NEWS_TYPE + " = ? ",
                new String[]{newsType}, null, null, null, null);
        //   Cursor cursor = database.rawQuery("select rowid,* from newsinfo where news_type = ? ", new String[]{newsType});
        List<News> lists = null;
        boolean isHaveCache = false;
        if (cursor != null) {
            lists = new ArrayList<News>();
            while (cursor.moveToNext()) {
                News news = new News();
                //               int rowid = cursor.getInt(cursor.getColumnIndex("rowid"));
                news.setTitle(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_TITLE)));
                news.setAuthor_name(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_AUTHOR_NAME)));
                news.setUrl(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_URL)));
                news.setDate(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_DATE)));
                news.setPicPath(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_PICPATH)));
                news.setIs_collected(cursor.getInt(cursor.getColumnIndex(NewsTable.NEWS_TABLE_IS_COLLECTION)) == 1);
                lists.add(news);
                //             LogUtils.d(CommonInfo.TAG, "-->read" + rowid + " " + news.getTitle() + " " + news.is_collected());
                isHaveCache = true;
            }
        }
        if (!isHaveCache) lists = null;
        LogUtils.d(CommonInfo.TAG, "-->" + lists);
        return lists;
    }

    /**
     * 更新数据库的缓存
     *
     * @param news     新的新闻
     * @param newsType 新闻类型
     */
    public void updataNewsCacheToDatabase(List<News> news, String newsType) {
        deleteNewsCacheFromDataBase(newsType);
        addNewsCacheToDataBase(news, newsType);
    }

    /**
     * 读取收藏的新闻项
     *
     * @return
     */
    public List<News> readCollectionNews() {
        getConnected();
        Cursor cursor = database.query(NEWS_TABLE_NAME, null, NEWS_TABLE_IS_COLLECTION + " = ? ",
                new String[]{"1"}, null, null, null, null);
        List<News> lists = new ArrayList<News>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                News news = new News();
                news.setTitle(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_TITLE)));
                news.setAuthor_name(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_AUTHOR_NAME)));
                news.setUrl(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_URL)));
                news.setDate(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_DATE)));
                news.setPicPath(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_PICPATH)));
                news.setIs_collected(cursor.getInt(cursor.getColumnIndex(NEWS_TABLE_IS_COLLECTION)) == 1);
                lists.add(news);
            }
        }
        return lists;
    }

    /**
     * 添加收藏新闻项
     *
     * @param title
     * @return boolean
     */
    public boolean addNewsCollectionToDataBase(String title) {
        getConnected();
        int result = -1;
        if (title != null) {
            ContentValues values = new ContentValues();
            values.put(NEWS_TABLE_IS_COLLECTION, "1");
            result = database.update(NEWS_TABLE_NAME, values, NEWS_TABLE_TITLE + " = ? ", new String[]{title});
        }
        LogUtils.d(CommonInfo.TAG, "asdff" + result);
        return result == -1 ? false : true;
    }

    /**
     * 移除收藏新闻项
     *
     * @param title
     * @return boolean
     */
    public boolean deleteNewsCollectionFromDataBase(String title) {
        getConnected();
        int result = -1;
        if (title != null) {
            ContentValues values = new ContentValues();
            values.put(NEWS_TABLE_IS_COLLECTION, "0");
            result = database.update(NEWS_TABLE_NAME, values, NEWS_TABLE_TITLE + " = ? ", new String[]{title});
        }
        return result == -1 ? false : true;
    }

    /**
     * 微信精选添加缓存
     *
     * @param weiChats
     */
    public void addWeiChatCaCheToDataBase(List<WeiChat> weiChats) {
        getConnected();
        long re = -1;
        if (weiChats != null) {
            for (WeiChat n : weiChats) {
                StringBuilder sql = new StringBuilder();
                sql.append("insert or replace into ").append(WeiChatTable.WEICHAT_TABLE_NAME).append(" ( ").append(WeiChatTable.WEICHAT_TABLE_ID + ",")
                        .append(WEICHAT_TABLE_TITLE + "," + WeiChatTable.WEICHAT_TABLE_PIC_PATH + "," + WeiChatTable.WEICHAT_TABLE__SOURCE
                                + "," + WeiChatTable.WEICHAT_TABLE_URL + "," + WeiChatTable.WEICHAT_PAGE + "," + WeiChatTable.WEICHAT_IS_COLLECTED)
                        .append(" ) values( ? ").append(",").append("?,?,?,?,?").append(",").append("(").append("select ")
                        .append(WeiChatTable.WEICHAT_IS_COLLECTED).append(" from ").append(WeiChatTable.WEICHAT_TABLE_NAME).append(" where ").append(WeiChatTable.WEICHAT_TABLE_ID).append(" = " + "?" + ") )");
                database.execSQL(sql.toString(), new Object[]{n.getId(), n.getTitle(), n.getPicPath(), n.getSource(), n.getUrl(), n.getPage(), n.getId()});
                LogUtils.d(CommonInfo.TAG, "--->" + sql.toString());
            }

        }
    }

    /**
     * 从数据库读取精选缓存
     *
     * @param page 请求页
     * @return List<WeiChat> 新闻缓存项
     */
    public List<WeiChat> readWeiChatCacheFromDatabase(int page) {
        getConnected();
        Cursor cursor = database.query(WeiChatTable.WEICHAT_TABLE_NAME, null, WEICHAT_PAGE + " = ? and (" + WEICHAT_IS_COLLECTED + " = ? or " + WEICHAT_IS_COLLECTED + " is null )",
                new String[]{page + "", "0"}, null, null, null, null);
        //   Cursor cursor = database.rawQuery("select rowid,* from newsinfo where news_type = ? ", new String[]{newsType});
        //     LogUtils.d(CommonInfo.TAG, "-->read database" + " " + );
        List<WeiChat> lists = null;
        boolean isHaveCache = false;
        if (cursor != null) {
            lists = new ArrayList<WeiChat>();
            while (cursor.moveToNext()) {
                WeiChat weiChat = new WeiChat();
                //               int rowid = cursor.getInt(cursor.getColumnIndex("rowid"));
                weiChat.setId(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE_ID)));
                weiChat.setSource(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE__SOURCE)));
                weiChat.setTitle(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE_TITLE)));
                weiChat.setPicPath(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE_PIC_PATH)));
                weiChat.setUrl(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE_URL)));
                weiChat.setIs_collected(cursor.getInt(cursor.getColumnIndex(WEICHAT_IS_COLLECTED)) == 1);
                weiChat.setPage(cursor.getInt(cursor.getColumnIndex(WEICHAT_PAGE)));
                lists.add(weiChat);
                LogUtils.d(CommonInfo.TAG, "-->read" + " " + weiChat.getTitle() + " " + weiChat.is_collected());
                isHaveCache = true;
            }
        }
        if (!isHaveCache) lists = null;
        LogUtils.d(CommonInfo.TAG, "-->" + lists);
        return lists;
    }

    /**
     * 读取收藏的精选项
     *
     * @return
     */
    public List<WeiChat> readCollectionWeiChat() {
        getConnected();
        Cursor cursor = database.query(WeiChatTable.WEICHAT_TABLE_NAME, null, WeiChatTable.WEICHAT_IS_COLLECTED + " = ? ",
                new String[]{"1"}, null, null, null, null);
        List<WeiChat> lists = new ArrayList<WeiChat>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                WeiChat weiChat = new WeiChat();
                weiChat.setTitle(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE_TITLE)));
                weiChat.setSource(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE__SOURCE)));
                weiChat.setUrl(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE_URL)));
                weiChat.setPicPath(cursor.getString(cursor.getColumnIndex(WEICHAT_TABLE_PIC_PATH)));
                weiChat.setIs_collected(cursor.getInt(cursor.getColumnIndex(WEICHAT_IS_COLLECTED)) == 1);
                weiChat.setPage(cursor.getInt(cursor.getColumnIndex(WEICHAT_PAGE)));
                lists.add(weiChat);
            }
        }
        return lists;
    }

    /**
     * 移除微信精选缓存
     *
     * @return
     */
    public boolean deleteWeiChatCacheFromDataBase() {
        getConnected();
        int res = database.delete(WeiChatTable.WEICHAT_TABLE_NAME, NewsTable.NEWS_TABLE_IS_COLLECTION + " = ? or  "
                + NEWS_TABLE_IS_COLLECTION + " is null ", new String[]{"0"});
        return res == -1 ? false : true;
    }

    /**
     * 添加收藏精选项
     *
     * @param title
     * @return boolean
     */
    public boolean addWeiChatCollectionToDataBase(String title) {
        getConnected();
        int result = -1;
        if (title != null) {
            ContentValues values = new ContentValues();
            values.put(WEICHAT_IS_COLLECTED, "1");
            result = database.update(WEICHAT_TABLE_NAME, values, WEICHAT_TABLE_TITLE + " = ? ", new String[]{title});
        }
        LogUtils.d(CommonInfo.TAG, "asdff" + result);
        return result == -1 ? false : true;
    }

    /**
     * 移除收藏精选项
     *
     * @param title
     * @return boolean
     */
    public boolean deleteWeiChatCollectionFromDataBase(String title) {
        getConnected();
        int result = -1;
        if (title != null) {
            ContentValues values = new ContentValues();
            values.put(WeiChatTable.WEICHAT_IS_COLLECTED, "0");
            result = database.update(WeiChatTable.WEICHAT_TABLE_NAME, values, WeiChatTable.WEICHAT_TABLE_TITLE + " = ? ", new String[]{title});
        }
        return result == -1 ? false : true;
    }


    /**
     * 段子添加缓存
     *
     * @param jokes
     */
    public void addJokeCaCheToDataBase(List<Joke> jokes) {
        getConnected();
        long re = -1;
        if (jokes != null) {
            for (Joke n : jokes) {
                StringBuilder sql = new StringBuilder();
                sql.append("insert or replace into ").append(JokeTable.JOKE_TABLE_NAME).append(" ( ").append(JokeTable.JOKE_TABLE_ID + ",")
                        .append(JokeTable.JOKE_TABLE_CONTENT + "," + JokeTable.JOKE_TABLE_UPDATA_TIME + "," + JokeTable.JOKE_TABLE_PAGE
                                + "," + JOKE_TABLE_IS_COLLECTED).append(" ) values( ").append("?").append(",")
                        .append(" ? , ? , ? ,").append("(").append("select ")
                        .append(JOKE_TABLE_IS_COLLECTED).append(" from ").append(JokeTable.JOKE_TABLE_NAME).append(" where ").append(JokeTable.JOKE_TABLE_ID).append(" = " + "?" + ") )");
                database.execSQL(sql.toString(), new Object[]{n.getHashId(), n.getContent(), n.getDate(), n.getPage(), n.getHashId()});
                LogUtils.d(CommonInfo.TAG, "--->" + sql.toString());
            }

        }
    }

    /**
     * 读取段子的缓存
     *
     * @param page
     * @return List<Joke>
     */
    public List<Joke> readJokeCacheFromDataBase(int page) {
        getConnected();
        Cursor cursor = database.query(JokeTable.JOKE_TABLE_NAME, null, JokeTable.JOKE_TABLE_PAGE + " = ? ",
                new String[]{page + ""}, null, null, JokeTable.JOKE_TABLE_PAGE + " asc  ", null);
        //   Cursor cursor = database.rawQuery("select rowid,* from newsinfo where news_type = ? ", new String[]{newsType});
        List<Joke> lists = null;
        boolean isHaveCache = false;
        if (cursor != null) {
            lists = new ArrayList<Joke>();
            while (cursor.moveToNext()) {
                Joke joke = new Joke();
                //               int rowid = cursor.getInt(cursor.getColumnIndex("rowid"));
                joke.setHashId(cursor.getString(cursor.getColumnIndex(JokeTable.JOKE_TABLE_ID)));
                joke.setContent(cursor.getString(cursor.getColumnIndex(JokeTable.JOKE_TABLE_CONTENT)));
                joke.setDate(cursor.getString(cursor.getColumnIndex(JokeTable.JOKE_TABLE_UPDATA_TIME)));
                joke.setIs_collected(cursor.getInt(cursor.getColumnIndex(JOKE_TABLE_IS_COLLECTED)) == 1);
                joke.setPage(cursor.getInt(cursor.getColumnIndex(JokeTable.JOKE_TABLE_PAGE)));
                lists.add(joke);
                //             LogUtils.d(CommonInfo.TAG, "-->read" + rowid + " " + weiChat.getTitle() + " " + weiChat.is_collected());
                isHaveCache = true;
            }
        }
        if (!isHaveCache) lists = null;
        LogUtils.d(CommonInfo.TAG, "-->" + lists);
        return lists;
    }

    /**
     * 删除所有段子缓存
     */
    public void deleteJokeCacheFromDatabase() {
        getConnected();
        database.delete(JokeTable.JOKE_TABLE_NAME, JOKE_TABLE_IS_COLLECTED + " = ? or  "
                + JOKE_TABLE_IS_COLLECTED + " is null ", new String[]{"0"});
    }

    /**
     * 更新段子缓存
     *
     * @param jokes
     */
    public void updateJokeCacheFromDataBase(List<Joke> jokes) {
        deleteJokeCacheFromDatabase();
        addJokeCaCheToDataBase(jokes);
    }

    /**
     * 读取收藏的段子
     *
     * @return
     */
    public List<Joke> readCollectionJokes() {
        getConnected();
        Cursor cursor = database.query(JOKE_TABLE_NAME, null, JOKE_TABLE_IS_COLLECTED + " = ? ",
                new String[]{"1"}, null, null, null, null);
        List<Joke> lists = new ArrayList<Joke>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Joke joke = new Joke();
                joke.setContent(cursor.getString(cursor.getColumnIndex(JOKE_TABLE_CONTENT)));
                joke.setHashId(cursor.getString(cursor.getColumnIndex(JOKE_TABLE_ID)));
                joke.setDate(cursor.getString(cursor.getColumnIndex(JOKE_TABLE_UPDATA_TIME)));
                joke.setIs_collected(cursor.getInt(cursor.getColumnIndex(JOKE_TABLE_IS_COLLECTED)) == 1);
                joke.setPage(cursor.getInt(cursor.getColumnIndex(JOKE_TABLE_PAGE)));
                lists.add(joke);
            }
        }
        return lists;
    }

    /**
     * 添加段子收藏
     *
     * @param hashID
     * @return boolean
     */
    public boolean addJokeCollectionToDataBase(String hashID) {
        getConnected();
        int result = -1;
        if (hashID != null) {
            ContentValues values = new ContentValues();
            values.put(JOKE_TABLE_IS_COLLECTED, "1");
            result = database.update(JOKE_TABLE_NAME, values, JOKE_TABLE_ID + " = ? ", new String[]{hashID});
        }
        LogUtils.d(CommonInfo.TAG, "asdff" + result);
        return result == -1 ? false : true;
    }


    /**
     * 移除段子收藏项
     *
     * @param hashID
     * @return boolean
     */
    public boolean deleteJokeCollectionFromDataBase(String hashID) {
        getConnected();
        int result = -1;
        if (hashID != null) {
            ContentValues values = new ContentValues();
            values.put(JOKE_TABLE_IS_COLLECTED, "0");
            result = database.update(JOKE_TABLE_NAME, values, JOKE_TABLE_ID + " = ? ", new String[]{hashID});
        }
        return result == -1 ? false : true;
    }
}