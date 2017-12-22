package com.andywang.ulife.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }

}
