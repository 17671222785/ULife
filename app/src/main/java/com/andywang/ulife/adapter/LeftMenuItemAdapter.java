package com.andywang.ulife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.MenuItemInfo;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/3.
 * 左侧侧滑菜单项的自定义适配器
 */

public class LeftMenuItemAdapter extends BaseAdapter {
    /**
     * 菜单项的信息
     */
    private List<MenuItemInfo> mLists;
    /**
     * 上下文对象
     */
    private Context mContext;

    public LeftMenuItemAdapter(Context context, List<MenuItemInfo> lists) {
        mLists = lists;
        mContext = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        MenuItemHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.left_menu_adapter_item, null);
            holder = new MenuItemHolder();
            holder.pic = view.findViewById(R.id.menu_picture);
            holder.content = view.findViewById(R.id.menu_content);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (MenuItemHolder) view.getTag();
        }
        holder.pic.setImageResource(mLists.get(position).getImageId());
        holder.content.setText(mLists.get(position).getNameId());
        return view;
    }

    /**
     * 布局重用的优化类
     */
    class MenuItemHolder {
        ImageView pic;
        TextView content;
    }
}
