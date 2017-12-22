package com.andywang.ulife.entity.calendar.bean;

import java.io.Serializable;

/**
 * Created by parting_soul on 2016/11/3.
 * 微信精选
 */

public class WeiChat implements Serializable {

    private String title;

    private String picPath;

    private String id;

    private String url;

    private String source;

    private boolean is_collected;

    private int page;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean is_collected() {
        return is_collected;
    }

    public void setIs_collected(boolean is_collected) {
        this.is_collected = is_collected;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
