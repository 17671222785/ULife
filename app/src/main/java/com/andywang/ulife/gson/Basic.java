package com.andywang.ulife.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

    }

}
