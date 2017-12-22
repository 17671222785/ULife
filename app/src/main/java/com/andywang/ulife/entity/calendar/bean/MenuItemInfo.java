package com.andywang.ulife.entity.calendar.bean;

/**
 * Created by parting_soul on 2016/10/3.
 * 每一个菜单项所带的信息
 */

public class MenuItemInfo {
    /**
     * 菜单标题的字符串的id
     */
    private int nameId;

    /**
     * 菜单图片的id
     */
    private int imageId;

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
