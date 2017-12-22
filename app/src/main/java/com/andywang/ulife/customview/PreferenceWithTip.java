package com.andywang.ulife.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andywang.ulife.R;


/**
 * Created by parting_soul on 2016/10/23.
 * 自定义菜单项UI
 */

public class PreferenceWithTip extends Preference {
    private String preTitle;

    private String preTip;

    private TextView titleView;

    private TextView tipView;

    public PreferenceWithTip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreferenceWithTip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.preferencetitletip);
        preTitle = array.getString(R.styleable.preferencetitletip_pretitle);
        preTip = array.getString(R.styleable.preferencetitletip_pretip);
        array.recycle();
    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(R.layout.preference_left_right_textview,
                parent, false);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        //所有更新UI的操作放在一起
        titleView = (TextView) view.findViewById(R.id.pre_title);
        tipView = (TextView) view.findViewById(R.id.pre_tip);
        titleView.setText(preTitle);
        tipView.setText(preTip);
    }

    public void setTip(String tip) {
        preTip = tip;
        notifyChanged(); //必须通知更新UI
    }

    public void setTitle(String title) {
        preTitle = title;
        notifyChanged();
    }
}
