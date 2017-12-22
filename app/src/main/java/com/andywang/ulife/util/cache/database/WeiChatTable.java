package com.andywang.ulife.util.cache.database;

/**
 * Created by parting_soul on 2016/11/4.
 */

public class WeiChatTable {
    /**
     * 微信精选表
     */
    public static final String WEICHAT_TABLE_NAME = "weichatinfo";

    /**
     * _id
     */
    public static final String WEICHAT_TABLE_ID = "_id";

    /**
     * 标题
     */
    public static final String WEICHAT_TABLE_TITLE = "title";

    /**
     * 图片url
     */
    public static final String WEICHAT_TABLE_PIC_PATH = "pic_path";

    /**
     * 来源
     */
    public static final String WEICHAT_TABLE__SOURCE = "source";

    /**
     * 网页url
     */
    public static final String WEICHAT_TABLE_URL = "url";

    /**
     * 是否被收藏
     */
    public static final String WEICHAT_IS_COLLECTED = "is_collected";

    /**
     * 当前页
     */
    public static final String WEICHAT_PAGE = "page";


    public static final String DELETE_WEICHAT_DATA = "delete from " + WEICHAT_TABLE_NAME + " where " + WEICHAT_IS_COLLECTED
            + " = ? or " + WEICHAT_IS_COLLECTED + " is null";

    /**
     * 建表语句
     */
    public static final String CREATE_WEICHAT_TABLE = "create table " + WEICHAT_TABLE_NAME + "("
            + WEICHAT_TABLE_ID + " text primary key ,"
            + WEICHAT_TABLE_TITLE + " text ,"
            + WEICHAT_TABLE_PIC_PATH + " text,"
            + WEICHAT_TABLE__SOURCE + " text,"
            + WEICHAT_TABLE_URL + " text,"
            + WEICHAT_IS_COLLECTED + " integer default 0,"
            + WEICHAT_PAGE + " integer " + ")";
}
