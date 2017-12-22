package com.andywang.ulife.entity.calendar.bean;

import java.io.Serializable;

/**
 * Created by parting_soul on 2016/11/5.
 */

public class Joke implements Serializable {
    private String content;

    private boolean is_collected;

    private String date;

    private String hashId;

    private int page;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean is_collected() {
        return is_collected;
    }

    public void setIs_collected(boolean is_collected) {
        this.is_collected = is_collected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
