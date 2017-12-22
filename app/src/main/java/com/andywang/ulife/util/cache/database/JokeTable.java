package com.andywang.ulife.util.cache.database;

/**
 * Created by parting_soul on 2016/11/6.
 */

public class JokeTable {
    public static final String JOKE_TABLE_NAME = "jokeinfo";

    public static final String JOKE_TABLE_ID = "_id";

    public static final String JOKE_TABLE_CONTENT = "content";

    public static final String JOKE_TABLE_UPDATA_TIME = "update_time";

    public static final String JOKE_TABLE_IS_COLLECTED = "is_collected";

    public static final String JOKE_TABLE_PAGE = "page";


    /**
     * 删除表中的缓存数据
     */
    public static final String DELETE_ALL_JOKE_CACHE = "delete from " + JOKE_TABLE_NAME + " where " + JOKE_TABLE_IS_COLLECTED
            + " = ? or " + JOKE_TABLE_IS_COLLECTED + " is null ";


    public static final String CREATE_JOKE_TABLE = "create table " + JOKE_TABLE_NAME + "("
            + JOKE_TABLE_ID + " text primary key,"
            + JOKE_TABLE_CONTENT + " text,"
            + JOKE_TABLE_UPDATA_TIME + " text,"
            + JOKE_TABLE_PAGE + " integer ,"
            + JOKE_TABLE_IS_COLLECTED + " integer default 0 " + ")";

}
