package com.andywang.ulife.entity.calendar.utils;

/**
 * 数组操作工具类
 * 
 * Utils of data operation
 *
 * Created by andyWang on 2017/11/29 0029.
 * 邮箱：393656489@qq.com
 */
public final class DataUtils {
    private DataUtils(){}
    /**
     * 一维数组转换为二维数组
     *
     * @param src    ...
     * @param row    ...
     * @param column ...
     * @return ...
     */
    public static String[][] arraysConvert(String[] src, int row, int column) {
        String[][] tmp = new String[row][column];
        for (int i = 0; i < row; i++) {
            tmp[i] = new String[column];
            System.arraycopy(src, i * column, tmp[i], 0, column);
        }
        return tmp;
    }
}
