package com.andywang.ulife.entity.calendar.entities;

/**
 * 日历数据实体
 * 封装日历绘制时需要的数据
 * 
 * Entity of calendar
 *
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public class DPInfo {
    public String strG, strF;
    public boolean isHoliday;
    public boolean isChoosed;
    public boolean isToday, isWeekend;
    public boolean isSolarTerms, isFestival, isDeferred;
    public boolean isDecorBG;
    public boolean isDecorTL, isDecorT, isDecorTR, isDecorL, isDecorR;
}