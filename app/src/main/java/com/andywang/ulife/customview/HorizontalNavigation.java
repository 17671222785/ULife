package com.andywang.ulife.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by parting_soul on 2016/10/4.
 * 自定义水平滚动视图，用于横向导航
 */

public class HorizontalNavigation extends HorizontalScrollView {

    public HorizontalNavigation(Context context) {
        super(context);
    }

    public HorizontalNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
