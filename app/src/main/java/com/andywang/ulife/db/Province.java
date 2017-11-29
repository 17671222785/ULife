package com.andywang.ulife.db;

import org.litepal.crud.DataSupport;

/**
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */

/**
 * 存放省的数据信息
 */
public class Province extends DataSupport {

    /**
     * id字段
     */
    private int id;

    /**
     * 省的名字
     */
    private String provinceName;

    /**
     * 省的代号
     */
    private int provinceCode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
