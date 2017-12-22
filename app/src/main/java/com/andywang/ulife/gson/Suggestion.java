package com.andywang.ulife.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {

        @SerializedName("txt")
        public String info;

    }

    public class CarWash {

        @SerializedName("txt")
        public String info;

    }

    public class Sport {

        @SerializedName("txt")
        public String info;

    }

}
