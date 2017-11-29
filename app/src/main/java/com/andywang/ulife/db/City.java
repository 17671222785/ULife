package com.andywang.ulife.db;

import org.litepal.crud.DataSupport;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */

/**
 * 存放市的信息
 */
public class City extends DataSupport {

    /**
     * id字段
     */
    private int id;

    /**
     * 市的名字
     */
    private String cityName;

    /**
     * 市的代号
     */
    private int cityCode;

    /**
     * 该市所属省的id
     */
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
