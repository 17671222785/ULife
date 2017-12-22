package com.andywang.ulife.util.cache.database;

/**
 * Created by parting_soul on 2016/11/4.
 */

public class NewsTable {
    /**
     * 表名
     */
    public static final String NEWS_TABLE_NAME = "newsinfo";

    /**
     * 主键_id
     */
    public static final String NEWS_TABLE_ID = "_id";

    /**
     * 新闻标题
     */
    public static final String NEWS_TABLE_TITLE = "title";

    /**
     * 图片路径
     */
    public static final String NEWS_TABLE_PICPATH = "pic_path";

    /**
     * 新闻url
     */
    public static final String NEWS_TABLE_URL = "url";

    /**
     * 新闻发布日期
     */
    public static final String NEWS_TABLE_DATE = "date";

    /**
     * 作者名字
     */
    public static final String NEWS_TABLE_AUTHOR_NAME = "author_name";

    /**
     * 新闻类型
     */
    public static final String NEWS_TABLE_NEWS_TYPE = "news_type";

    /**
     * 是否被收藏
     */
    public static final String NEWS_TABLE_IS_COLLECTION = "news_is_collected";

    /**
     * 删除表中的数据
     */
    public static final String DELETE_NEWS_TABLE_CACHE = "delete from " + NEWS_TABLE_NAME + " where " + NEWS_TABLE_IS_COLLECTION
            + " = ? or " + NEWS_TABLE_IS_COLLECTION + " is null ";

    /**
     * 建表语句
     */
    public static final String CREATE_NEWS_TABLE = "create table " + NewsTable.NEWS_TABLE_NAME + "("
            + NewsTable.NEWS_TABLE_TITLE + " text primary key,"
            + NewsTable.NEWS_TABLE_URL + " text,"
            + NewsTable.NEWS_TABLE_PICPATH + " text,"
            + NewsTable.NEWS_TABLE_DATE + " text,"
            + NewsTable.NEWS_TABLE_AUTHOR_NAME + " text,"
            + NewsTable.NEWS_TABLE_NEWS_TYPE + " text,"
            + NewsTable.NEWS_TABLE_IS_COLLECTION + " integer default 0 " + ")";

}
