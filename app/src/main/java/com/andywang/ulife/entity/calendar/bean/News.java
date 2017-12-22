package com.andywang.ulife.entity.calendar.bean;

import java.io.Serializable;

/**
 * Created by parting_soul on 2016/10/3.
 * 新闻类
 */

public class News implements Serializable {
    /**
     * 新闻的标题
     */
    private String title;
    /**
     * 新闻对应的图片路径
     */
    private String picPath;
    /**
     * 新闻的url
     */
    private String url;
    /**
     * 发布新闻的日期
     */
    private String date;

    /**
     * 是否被收藏
     */
    private boolean is_collected;

    /**
     * 作者名字
     */
    private String author_name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public boolean is_collected() {
        return is_collected;
    }

    public void setIs_collected(boolean is_collected) {
        this.is_collected = is_collected;
    }
}
