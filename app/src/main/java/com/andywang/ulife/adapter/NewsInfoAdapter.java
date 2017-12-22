package com.andywang.ulife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.News;
import com.andywang.ulife.util.image.ImageLoader;
import com.andywang.ulife.util.style.FontChangeManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

import java.util.List;

/**
 * Created by parting_oul on 2016/10/4.
 * 新闻内容适配器
 */

public class NewsInfoAdapter extends BaseFragmentAdapter<News> implements AbsListView.OnScrollListener {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 图片加载类
     */
    private ImageLoader mImageLoader;


    /**
     * 新闻数据项
     */
    private ListView mListView;


    private static int textStyleId;

    public NewsInfoAdapter(Context context, List<News> lists, ListView listView) {
        mContext = context;
        mLists = lists;
        mListView = listView;
        mImageLoader = ImageLoader.newInstance(context);
        getPicUrl();
        //为listview添加滚动监听
        listView.setOnScrollListener(this);
        textStyleId = FontChangeManager.changeItemTitleFontSize();
    }

    /**
     * 得到所有图片的URL地址
     */
    @Override
    public void getPicUrl() {
        IMAGE_URLS = new String[mLists.size()];
        for (int i = 0; i < mLists.size(); i++) {
            IMAGE_URLS[i] = mLists.get(i).getPicPath();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        NewsHolder holder = null;
        if (convertView == null) {
            //没有可重用的布局，就加载一个布局
            view = LayoutInflater.from(mContext).inflate(R.layout.news_detail__listview_item, null);
            //找到布局中所有的UI控件并且绑定在Holder中
            holder = new NewsHolder();
            holder.title = view.findViewById(R.id.news_title);
            holder.pic = view.findViewById(R.id.news_pic);
            holder.authorName = view.findViewById(R.id.news_author_name);
            holder.date = view.findViewById(R.id.news_date);
            //给新加载的布局绑定Holder，以便布局重用
            view.setTag(holder);
        } else {
            //有可重用的布局
            view = convertView;
            //得到绑定的Holder
            holder = (NewsHolder) view.getTag();
        }
        //为UI控件设置值
        holder.title.setText(mLists.get(position).getTitle());
        holder.title.setTextAppearance(mContext, textStyleId);
        holder.authorName.setText(mLists.get(position).getAuthor_name());
        holder.date.setText(mLists.get(position).getDate());
//        holder.pic.setImageResource(R.mipmap.imageview_default_bc);

        String url = mLists.get(position).getPicPath();
        LogUtils.d(CommonInfo.TAG, "---->" + url + " ++ " + holder.pic);
        holder.pic.setTag(url);
        mImageLoader.loadImage(url, holder.pic);
//        LogUtils.d(TAG, "getView: " + mLists.get(position).getTitle() + " " + position + " " + mLists.get(position).getPicPath());
        return view;
    }

    class NewsHolder {
        ImageView pic;
        TextView title, date, authorName;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //如果停止滑动就加载当前可见项的图片
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mImageLoader.loadImage(mStart, mEnd, mListView, this);
        } else {
            mImageLoader.cancelAllAsyncTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        LogUtils.i(CommonInfo.TAG, "NewsInfoAdapter-->onScroll-->onScroll start = " + mStart + " end = " + mEnd);
        LogUtils.i(CommonInfo.TAG, "NewsInfoAdapter-->" + isFirstIn + " " + visibleItemCount + " " + mCanLoagImage);
        if (isFirstIn && visibleItemCount > 0 && mCanLoagImage) {
            mImageLoader.loadImage(mStart, mEnd, mListView, this);
            isFirstIn = false;
        }
    }

}
