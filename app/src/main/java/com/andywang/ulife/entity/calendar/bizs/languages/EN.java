package com.andywang.ulife.entity.calendar.bizs.languages;

/**
 * 英文的默认实现类
 * 如果你想实现更多的语言请参考Language{@link DPLManager}
 *
 * The implementation class of english.
 * You can refer to Language{@link DPLManager} if you want to define more language.
 *
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public class EN extends DPLManager {
    @Override
    public String[] titleMonth() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    @Override
    public String titleEnsure() {
        return "Ok";
    }

    @Override
    public String titleBC() {
        return "B.C.";
    }

    @Override
    public String[] titleWeek() {
        return new String[]{"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    }
}
