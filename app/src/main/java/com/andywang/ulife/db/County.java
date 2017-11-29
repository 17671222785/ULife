package com.andywang.ulife.db;

import org.litepal.crud.DataSupport;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */

/**
 * 记录县的信息
 */
public class County extends DataSupport {

    /**
     * id字段
     */
    private int id;

    /**
     * 县的名字
     */
    private String countyName;

    /**
     * 县所对应的天气id
     */
    private String weatherId;

    /**
     * 该县所属市的id
     */
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
