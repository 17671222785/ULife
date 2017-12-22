package com.andywang.ulife.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/3.
 */

public abstract class BaseFragmentAdapter<T> extends BaseAdapter {

    /**
     * 数据源
     */
    protected List<T> mLists;

    /**
     * 图片的url地址数组,注意不能用静态
     */
    public String[] IMAGE_URLS;

    /**
     * listview可见项的起始位置
     */
    protected int mStart;

    /**
     * listview可见项的终止位置
     */
    protected int mEnd;

    /**
     * 是否可以加载图片
     */
    boolean mCanLoagImage;

    /**
     * 是否是第一次打开
     */
    protected boolean isFirstIn = true;

    public abstract void getPicUrl();

    /**
     * 返回数据源
     *
     * @return T
     */
    public List<T> getData() {
        return mLists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置是否可以加载图片
     *
     * @param canLoagImage
     */
    public void setIsCanLoadImage(boolean canLoagImage) {
        mCanLoagImage = canLoagImage;
    }
}
